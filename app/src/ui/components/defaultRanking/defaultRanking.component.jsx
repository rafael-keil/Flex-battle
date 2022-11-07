import "./defaultRanking.style.css";
import { TROPHY, CROWN_FIRST } from "../../../assets";

export function DefaultRanking({ ranking }) {
  return (
    <div>
      <div className="default-ranking__container">
        <img
          src={CROWN_FIRST}
          className="default-ranking__crown-first"
          alt="coroa de primeiro colocado"
        />
        <div className="default-ranking__title">
          <p className="default-ranking__title-name">Os Melhores </p>
          <img
            src={TROPHY}
            className="default-ranking__title-img"
            alt="icone de troféu"
          />
        </div>

        {ranking.map((user, index) => (
          <div className="default-ranking__list-item">
            <div className="default-ranking__list-item-position">
              <p>{index + 1}</p>
            </div>
            <p className="default-ranking__list-item-name">{user.name}</p>
            <div className="default-ranking__list-item-points">
              <p>{user.ranking}</p>
              <p className="default-ranking__list-item-points-detail">pt</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
