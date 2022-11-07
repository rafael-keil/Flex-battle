import { useState } from "react";
import "./transformTip.style.css";
import { OPEN_TIP } from "../../../assets";

export function TransformTip({ text }) {
  const dicas = text.split("%1");
  const [numberTip, setNumberTip] = useState(null);

  function moreTips(index) {
    if (numberTip === index) {
      setNumberTip(null);
    } else {
      setNumberTip(index);
    }
  }

  function buttonVisible(index) {
    if (index !== numberTip) {
      return true;
    }
    return false;
  }

  function ReturnTip({ remaining, index }) {
    if (index === numberTip) {
      return (
        <ul className="transform-tip__description">
          {remaining.map((reman, index) => {
            const words = reman.split(":");
            return (
              <li key={index}>
                <span className="transform-tip__bold">{words[0]}:</span>
                {words[1]}
              </li>
            );
          })}
        </ul>
      );
    }
    return null;
  }

  return (
    <>
      <p className="transform-tip__first-title">
        Posicione os Donuts em seus respectivos pratos, utilizando propriedades
        de CSS, de acordo com as dicas abaixo:
      </p>
      {dicas.map((tip, index) => {
        const separateTitle = tip.split("%2");
        const title = separateTitle[0].split("%0");
        const remaining = separateTitle[1].split("%3");

        return (
          <div key={index}>
            <div className="transform-tip__order-title-button">
              <p className="transform-tip__title">
                <span className="transform-tip__bold">{title[0]}</span>{" "}
                {title[1]}
              </p>

              <button
                onClick={() => moreTips(index)}
                className="transform-tip__button"
              >
                <img
                  src={OPEN_TIP}
                  className={
                    buttonVisible(index) ? "" : "transform-tip__button-icon"
                  }
                  alt="flecha para abrir dicas"
                />
              </button>
            </div>
            <ReturnTip remaining={remaining} index={index} />
          </div>
        );
      })}
    </>
  );
}
