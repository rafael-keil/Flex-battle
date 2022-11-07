import { Donut, Plate } from "..";
import "./game.style.css";
import { useEffect, useState } from "react";
import { DEFAULT_DONUTS, DEFAULT_PLATES } from "../../../constants";

export function Game({ input, objects, answer, size }) {
  const [donuts, setDonuts] = useState([]);
  const [plates, setPlates] = useState([]);

  useEffect(() => {
    const arrayBase = [...Array(objects).keys()];
    const donutsList = arrayBase.map((item) => DEFAULT_DONUTS[item]);
    const platesList = arrayBase.map((item) => DEFAULT_PLATES[item]);

    setDonuts(donutsList);
    setPlates(platesList);
  }, [objects]);

  return (
    <div className={`game__background-game-${size} game__background-game`}>
      <div className="game__donut-style" style={input}>
        {donuts.map((donut) => {
          return <Donut key={donut} color={donut} size={size} />;
        })}
      </div>

      <div className="game__plate-style" style={Object.fromEntries(answer)}>
        {plates.map((plate) => {
          return <Plate key={plate} color={plate} size={size} />;
        })}
      </div>
    </div>
  );
}
