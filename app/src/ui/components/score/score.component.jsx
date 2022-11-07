import "./score.style.css";
import { STAR_OWNER_BATTLE, STAR_OPPONENT_BATTLE } from "../../../assets";
export function Score({
  ownerRank,
  ownerName,
  ownerPointStyle,
  points,
  opponentPointStyle,
  opponentPoints,
  opponentName,
  opponentRank,
}) {
  return (
    <div className="score__score">
      <div className="score__initial-name ">
        <div className="score__star-rank-battle-left">
          <img
            src={STAR_OWNER_BATTLE}
            className="score__star-image-left"
            alt="estrela do aliado"
          />
          <p className="score__rank-point">{ownerRank}</p>
        </div>
        <p className="score__initial-name-left">{ownerName}</p>
      </div>

      <h1 className={`score__score-point-owner-${ownerPointStyle}`}>
        {points}
      </h1>

      <h5>x</h5>

      <h1 className={`score__score-point-opponent-${opponentPointStyle}`}>
        {Math.max(opponentPoints, 0)}
      </h1>

      <div className="score__initial-name ">
        <p className="score__initial-name-right">{opponentName}</p>
        <div className="score__star-rank-battle-right">
          <img
            src={STAR_OPPONENT_BATTLE}
            className="score__star-image-right"
            alt="estrela do inimigo"
          />
          <p className="score__rank-point">{opponentRank}</p>
        </div>
      </div>
    </div>
  );
}
