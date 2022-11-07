import "./defaultInput.style.css";
import { useState } from "react";
import { EYE } from "../../../assets";

export function DefaultInput({ onChange, label, type, ...props }) {
  const [changedType, setChangedType] = useState(false);

  function handleChange(event) {
    onChange(event.target.value);
  }

  function handlePasword() {
    setChangedType(!changedType);
  }

  return (
    <div className="default-input">
      <input
        className="default-input__input"
        onChange={handleChange}
        type={changedType ? "text" : type}
        {...props}
        required
      />
      <div className="default-input__span">{label}</div>
      {type === "password" ? (
        <img
          className="default-input__img"
          src={EYE}
          alt="Olho para liberar visualisação de senha"
          onClick={handlePasword}
        />
      ) : null}
    </div>
  );
}
