import { useState, useEffect } from "react";
import {
  TextAreaGame,
  Game,
  MenuContainer,
  DefaultButton,
  Loading,
  GameResult,
} from "../../components";
import {
  kebabCaseStringIntoObjectCamelCase,
  validadeResponse,
} from "../../../utils";
import "./multiplayerGame.style.css";
import { useFbApi } from "../../../hooks";
import { BATTLE_ICON, DONUT_BIG } from "../../../assets";
import { Header } from "../header/header.component";
import { Score } from "../score/score.component";

export function MultiplayerGame({
  points,
  opponentPoints,
  handleSendInput,
  handleNextChallenge,
  challenge,
  ownerName,
  ownerRank,
  opponentName,
  opponentRank,
  opponentChallenge,
  opponentInput,
  endTime,
  token,
  opponentRankFinal,
  ownerRankFinal,
  winner,
}) {
  const [input, setInput] = useState("");
  const [timeLeft, setTimeLeft] = useState(125);
  const [timeStyle, setTimeStyle] = useState("");
  const [ownerPointStyle, setOwnerPointStyle] = useState(false);
  const [opponentPointStyle, setOpponentPointStyle] = useState(false);
  const [focus, setFocus] = useState(true);
  const fbApi = useFbApi();

  useEffect(() => {
    const newTimeLeft = Math.floor((endTime - Date.now()) / 1000);

    if (newTimeLeft > 0) {
      setTimeLeft(newTimeLeft);
    }
  }, [endTime]);

  useEffect(() => {
    setOwnerPointStyle(true);

    setTimeout(() => {
      setOwnerPointStyle(false);
    }, 1000);
  }, [points]);

  useEffect(() => {
    setOpponentPointStyle(true);

    setTimeout(() => {
      setOpponentPointStyle(false);
    }, 1000);
  }, [opponentPoints]);

  useEffect(() => {
    const tick = setInterval(() => {
      if (timeLeft > 0) {
        setTimeLeft(timeLeft - 1);
      }
      if (timeLeft === 0) {
        getWinner();
      }

      clearInterval(tick);
    }, 1000);

    async function getWinner() {
      try {
        await fbApi.getWinner(token);
      } catch {}
    }

    switch (timeLeft >= 0) {
      case timeLeft <= 15:
        setTimeStyle("red");
        break;

      case timeLeft <= 30:
        setTimeStyle("yellow-fast");
        break;

      case timeLeft <= 60:
        setTimeStyle("yellow");
        break;

      default:
        setTimeStyle("");
        break;
    }
  }, [timeLeft, fbApi]);

  function transformTime(time) {
    if (time > 120) {
      return "";
    }

    const minutes = "0" + Math.floor(time / 60);
    const seconds = time % 60;

    return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
  }

  function handleChangeInput(newInput) {
    setInput(newInput);
    handleSendInput(newInput);
  }

  function handleClick() {
    handleNextChallenge();
    setInput("");
    handleSendInput("");
    setFocus(!focus);
  }

  function initialTimer() {
    if (timeLeft > 120) {
      const timer = timeLeft - 120;

      return (
        <div className="multiplayer-game__initial-timer">
          <MenuContainer>
            <div className="multiplayer-game__initial-timer-container">
              <p className="multiplayer-game__initial-timer-description">
                A partida inicia em:
              </p>
              <p className="multiplayer-game__initial-timer-description">{`${timer} seg`}</p>
            </div>
          </MenuContainer>
        </div>
      );
    }
  }

  if (timeLeft === 0 || winner) {
    return (
      <>
        <GameResult
          opponentRankFinal={opponentRankFinal}
          ownerRankFinal={ownerRankFinal}
          winner={winner}
        />
      </>
    );
  }

  return (
    <div className="multiplayer-game__main">
      {initialTimer()}
      <Header img={BATTLE_ICON} menuButton={true} />
      <div className="multiplayer-game__game-left">
        {challenge ? (
          <div className="multiplayer-game__game-left-content">
            <Game
              input={kebabCaseStringIntoObjectCamelCase(input)}
              objects={challenge.objects}
              answer={challenge.answer}
              size={"medium"}
            />

            <div className="multiplayer-game__text-button">
              <img
                src={DONUT_BIG}
                className="multiplayer-game__donnut-back"
                alt="Donut Grande"
              />
              <TextAreaGame
                onChange={handleChangeInput}
                focus={focus}
                value={input}
              />

              <div className="multiplayer-game__button">
                {!validadeResponse(
                  challenge.answer,
                  kebabCaseStringIntoObjectCamelCase(input)
                ) ? (
                  <DefaultButton onClick={handleClick}>Proximo</DefaultButton>
                ) : null}
              </div>
            </div>
            <div className="multiplayer-game__color-back"></div>
          </div>
        ) : null}
      </div>

      <div className="multiplayer-game__game-right">
        <div
          className={`multiplayer-game__timer multiplayer-game__timer-${timeStyle}`}
        >
          <h2>{transformTime(timeLeft)}</h2>
        </div>

        <Score
          ownerRank={ownerRank}
          ownerName={ownerName}
          ownerPointStyle={ownerPointStyle}
          points={points}
          opponentPointStyle={opponentPointStyle}
          opponentPoints={opponentPoints}
          opponentName={opponentName}
          opponentRank={opponentRank}
        />

        {opponentChallenge ? (
          <div className="multiplayer-game__console-right">
            <Game
              input={kebabCaseStringIntoObjectCamelCase(opponentInput)}
              objects={opponentChallenge.objects}
              answer={opponentChallenge.answer}
              size={"small"}
            />
          </div>
        ) : null}
      </div>
    </div>
  );
}
