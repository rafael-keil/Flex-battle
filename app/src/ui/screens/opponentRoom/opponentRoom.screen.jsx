import { useState } from "react";
import { useFbApi } from "../../../hooks";
import { WS_URL } from "../../../constants";
import { Loading, MultiplayerGame, Snackbar } from "../../components";
import SockJsClient from "react-stomp";
import { OpponentRoomInfo } from "./components";
import { transformName } from "../../../utils";

export function OpponentRoom() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(false);
  const [challenges, setChallenges] = useState(null);
  const [nextChallenges, setNextChallenges] = useState(null);
  const [endTime, setEndTime] = useState(null);
  const [token, setToken] = useState("");
  const [play, setPlay] = useState(false);
  const [inputOpponent, setInputOpponent] = useState("");
  const [pointsOpponent, setPointsOpponent] = useState(0);
  const [pointsOwner, setPointsOwner] = useState(null);
  const [ownerName, setOwnerName] = useState("");
  const [ownerRank, setOwnerRank] = useState(0);
  const [opponentName, setOpponentName] = useState("");
  const [opponentRank, setOpponentRank] = useState(0);
  const [opponentRankFinal, setOpponentRankFinal] = useState(0);
  const [ownerRankFinal, setOwnerRankFinal] = useState(0);
  const [winner, setWinner] = useState(null);
  const fbApi = useFbApi();

  async function handleOpponentRoomSubmit(newToken) {
    setIsLoading(true);

    setToken(newToken);

    try {
      await fbApi.enterBattle(newToken);

      setPlay(true);
    } catch {
      setError(true);
      setIsLoading(false);
    }
  }

  async function onConnect() {
    try {
      await fbApi.getStartBattleChallenges(token);
    } catch {}
  }

  async function handleSendTopic(input) {
    try {
      await fbApi.inputOpponentBattle(token, input);
    } catch {}
  }

  function onMessageReceived(msg, topic) {
    switch (topic) {
      case `/room/${token}/owner/input`:
        setInputOpponent(msg.input);
        break;

      case `/room/${token}/owner/challenge`:
        if (nextChallenges?.challengeOwner) {
          const newChallengesOwner = {
            ...challenges,
            challengeOwner: nextChallenges.challengeOwner,
          };
          setChallenges(newChallengesOwner);
        }

        pointsOwner === null
          ? setPointsOwner(0)
          : setPointsOwner(pointsOwner + 1);

        const newNextChallengeOwner = {
          ...nextChallenges,
          challengeOwner: msg,
        };
        setNextChallenges(newNextChallengeOwner);
        break;

      case `/room/${token}/opponent/challenge`:
        const newNextChallengeOpponent = {
          ...nextChallenges,
          challengeOpponent: msg,
        };
        setNextChallenges(newNextChallengeOpponent);
        break;

      case `/room/${token}/winner`:
        setWinner(msg);
        setOpponentRankFinal(msg.opponentRanking);
        setOwnerRankFinal(msg.ownerRanking);
        break;

      case `/room/${token}`:
        setChallenges(msg);
        setPlay(true);
        break;

      case `/room/${token}/users`:
        setEndTime(Date.now() + 126000);
        setOpponentName(msg.opponent.username);
        setOpponentRank(msg.opponent.ranking);
        setOwnerName(msg.owner.username);
        setOwnerRank(msg.owner.ranking);
        break;

      default:
        break;
    }
  }

  function handleNextChallenge() {
    fbApi.getNewOpponentChallenge(token);

    setPointsOpponent(pointsOpponent + 1);

    const newChallengesOpponent = {
      ...challenges,
      challengeOpponent: nextChallenges.challengeOpponent,
    };
    setChallenges(newChallengesOpponent);
  }

  function handleClose() {
    setError(false);
  }

  if (challenges && isLoading) {
    setIsLoading(false);
  }

  return (
    <div>
      {play ? (
        <SockJsClient
          url={WS_URL}
          topics={[
            `/room/${token}`,
            `/room/${token}/owner/input`,
            `/room/${token}/owner/challenge`,
            `/room/${token}/opponent/challenge`,
            `/room/${token}/winner`,
            `/room/${token}/users`,
          ]}
          onConnect={onConnect}
          onMessage={(msg, topic) => onMessageReceived(msg, topic)}
        />
      ) : null}

      {challenges ? (
        <MultiplayerGame
          handleSendInput={handleSendTopic}
          handleNextChallenge={handleNextChallenge}
          challenge={challenges.challengeOpponent}
          ownerName={transformName(opponentName)}
          ownerRank={opponentRank}
          opponentName={transformName(ownerName)}
          opponentRank={ownerRank}
          opponentChallenge={challenges.challengeOwner}
          opponentInput={inputOpponent}
          endTime={endTime}
          points={pointsOpponent}
          opponentPoints={pointsOwner}
          token={token}
          opponentRankFinal={ownerRankFinal}
          ownerRankFinal={opponentRankFinal}
          winner={winner}
        />
      ) : (
        <OpponentRoomInfo handleOpponentRoomSubmit={handleOpponentRoomSubmit} />
      )}

      <Loading isLoading={isLoading} />
      <Snackbar
        text="Código não encontrado!"
        visibility={error}
        handleClose={handleClose}
        type="error"
      />
    </div>
  );
}
