import "./gameResult.style.css";
import { MenuContainer, PreviewRanking } from "../../components";
import { Header } from "../header/header.component";
import {
  BATTLE_ICON,
  LOSE_GAME_ARROW,
  LOSE_GAME_BACKGROUND,
  LOSE_GAME_IMAGE,
  WIN_GAME_ARROW,
  WIN_GAME_BACKGROUND,
  WIN_GAME_IMAGE,
  DRAW_GAME_BACKGROUND,
  DRAW_LINE,
} from "../../../assets";
import { useEffect, useState } from "react";
import { useFbApi } from "../../../hooks";
import { Loading } from "../loading/loading.component";

export function GameResult({ ownerRankFinal, opponentRankFinal, winner }) {
  const [ranking, setRanking] = useState([]);
  const [user, setUser] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [illustration, setIllustration] = useState();
  const [background, setBackground] = useState();
  const [title, setTitle] = useState();
  const [rankingStyle, setRankingStyle] = useState();
  const [arrow, setArrow] = useState();
  const [menuState, setMenuState] = useState(true);
  const fbApi = useFbApi();

  useEffect(() => {
    async function getResponse() {
      try {
        const responseRanking = await fbApi.geRanking();
        const responseUser = await fbApi.getUser();

        const topFive = responseRanking.filter(
          (userRanking, index) => index < 5
        );

        setRanking(topFive);
        setUser(responseUser);
        setIsLoading(false);
      } catch {}
    }

    if (winner) {
      checkWinner();
      getResponse();
      setMenuState(!menuState);
    }
  }, [fbApi, user, winner]);

  function checkWinner() {
    if (ownerRankFinal < opponentRankFinal) {
      setIllustration(LOSE_GAME_IMAGE);
      setBackground(LOSE_GAME_BACKGROUND);
      setArrow(LOSE_GAME_ARROW);
      setTitle("Você perdeu a partida!");
      setRankingStyle("loser");
    } else if (ownerRankFinal > opponentRankFinal) {
      setIllustration(WIN_GAME_IMAGE);
      setBackground(WIN_GAME_BACKGROUND);
      setArrow(WIN_GAME_ARROW);
      setTitle("Você ganhou a partida!");
      setRankingStyle("winner");
    } else {
      setIllustration(LOSE_GAME_IMAGE);
      setBackground(DRAW_GAME_BACKGROUND);
      setArrow("");
      setTitle("A partida empatou!");
      setRankingStyle("draw");
    }
  }

  return (
    <div className="game-result">
      <Header img={BATTLE_ICON} menuButton={true} userUpdate={menuState} />

      <MenuContainer>
        <div className="game-result__content">
          <div
            className="game-result__background"
            style={{
              backgroundImage: `url(${background})`,
            }}
          >
            {ownerRankFinal == 0 ? (
              <div className="game-result__image-draw">
                <div>
                  <img
                    src={LOSE_GAME_IMAGE}
                    alt="donut rosa pequeno"
                    className="game-result__small-image"
                  />
                  <img
                    src={LOSE_GAME_IMAGE}
                    alt="donut rosa pequeno"
                    className="game-result__small-image"
                  />
                </div>
                <img
                  src={DRAW_LINE}
                  alt="linha cinza em posição horizontal"
                  className="game-result__image-draw-line"
                />
              </div>
            ) : (
              <img
                src={illustration}
                className="game-result__illustration"
                alt="imagem principal da tela final"
              />
            )}
          </div>

          <div className="game-result__information">
            <div className="game-result__header">
              <p className="game-result__title">{title}</p>
              <div
                className={`game-result__value-default game-result__value-${rankingStyle}`}
              >
                <p className="game-result__value">
                  {ownerRankFinal > 0 ? "+" + ownerRankFinal : ownerRankFinal}
                </p>
                <p className="game-result__sub-title">PONTOS</p>
              </div>
            </div>

            <div className="game-result__user">
              <p>{user?.name}</p>
              <div className="game-result__arrow">
                {ownerRankFinal == 0 ? (
                  ""
                ) : (
                  <img src={arrow} alt="Flecha indicando pontuação" />
                )}
                <p>{user?.ranking}</p>
                <p className="game-result__pt">pt</p>
              </div>
            </div>
            <PreviewRanking ranking={ranking} />
          </div>
        </div>
      </MenuContainer>

      <Loading isLoading={isLoading} />
    </div>
  );
}
