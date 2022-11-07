import { useEffect, useState } from "react";
import { useFbApi } from "../../../hooks";
import { WS_URL } from "../../../constants";
import { Loading, MultiplayerGame } from "../../components/index";
import SockJsClient from "react-stomp";
import { OwnerRoomInfo } from "./components";
import { transformName } from "../../../utils";

export function OwnerRoom() {
  const [isloading, setIsLoading] = useState(true);
  const [challenges, setChallenges] = useState(null);
  const [nextChallenges, setNextChallenges] = useState(null);
  const [endTime, setEndTime] = useState(null);
  const [token, setToken] = useState(null);
  const [play, setPlay] = useState(false);
  const [inputOpponent, setInputOpponent] = useState("");
  const [pointsOpponent, setPointsOpponent] = useState(null);
  const [pointsOwner, setPointsOwner] = useState(0);
  const [ownerName, setOwnerName] = useState("");
  const [ownerRank, setOwnerRank] = useState(0);
  const [opponentName, setOpponentName] = useState("");
  const [opponentRank, setOpponentRank] = useState(0);
  const [opponentRankFinal, setOpponentRankFinal] = useState(0);
  const [ownerRankFinal, setOwnerRankFinal] = useState(0);
  const [winner, setWinner] = useState(null);

  const fbApi = useFbApi();

  useEffect(() => {
    async function getToken() {
      try {
        const response = await fbApi.getToken();

        setToken(response);
        setIsLoading(false);
      } catch {}
    }
    getToken();
  }, [fbApi]);

  async function handleSendTopic(input) {
    try {
      await fbApi.inputOwnerBattle(token, input);
    } catch {}
  }

  function onMessageReceived(msg, topic) {
    switch (topic) {
      case `/room/${token}/opponent/input`:
        setInputOpponent(msg.input);
        break;

      case `/room/${token}/owner/challenge`:
        const newNextChallengeOwner = {
          ...nextChallenges,
          challengeOwner: msg,
        };
        setNextChallenges(newNextChallengeOwner);
        break;

      case `/room/${token}/opponent/challenge`:
        if (nextChallenges?.challengeOpponent) {
          const newChallengesOpponent = {
            ...challenges,
            challengeOpponent: nextChallenges.challengeOpponent,
          };
          setChallenges(newChallengesOpponent);
        }

        pointsOpponent === null
          ? setPointsOpponent(0)
          : setPointsOpponent(pointsOpponent + 1);

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
    try {
      fbApi.getNewOwnerChallenge(token);
    } catch {}

    setPointsOwner(pointsOwner + 1);

    const newChallengesOwner = {
      ...challenges,
      challengeOwner: nextChallenges.challengeOwner,
    };
    setChallenges(newChallengesOwner);
  }

  function handleRenderGame() {
    if (play && challenges) {
      return (
        <MultiplayerGame
          handleSendInput={handleSendTopic}
          handleNextChallenge={handleNextChallenge}
          challenge={challenges.challengeOwner}
          ownerName={transformName(ownerName)}
          ownerRank={ownerRank}
          opponentName={transformName(opponentName)}
          opponentRank={opponentRank}
          opponentChallenge={challenges.challengeOpponent}
          opponentInput={inputOpponent}
          endTime={endTime}
          points={pointsOwner}
          opponentPoints={pointsOpponent}
          token={token}
          opponentRankFinal={opponentRankFinal}
          ownerRankFinal={ownerRankFinal}
          winner={winner}
        />
      );
    }

    return <OwnerRoomInfo token={token} />;
  }

  return (
    <main>
      {token ? (
        <SockJsClient
          url={WS_URL}
          topics={[
            `/room/${token}`,
            `/room/${token}/opponent/input`,
            `/room/${token}/owner/challenge`,
            `/room/${token}/opponent/challenge`,
            `/room/${token}/winner`,
            `/room/${token}/users`,
          ]}
          onMessage={(msg, topic) => onMessageReceived(msg, topic)}
        />
      ) : null}

      {handleRenderGame()}

      <Loading isLoading={isloading} />
    </main>
  );
}
