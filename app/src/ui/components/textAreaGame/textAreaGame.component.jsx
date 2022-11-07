import { useState, useRef, useEffect } from "react";
import "./textAreaGame.style.css";

export function TextAreaGame({ onChange, focus, ...props }) {
  const [rows, setRows] = useState(1);
  const inputRef = useRef();

  useEffect(() => {
    inputRef.current.focus();
  }, [focus]);

  function handleChange(event) {
    const value = event.target.value;
    const lines = value.split("\n");

    const lineSize = lines.every((line) => line.length < 35);

    if (lines.length <= 3 && lineSize) {
      onChange(event.target.value);
      setRows(lines.length);
    }
  }

  return (
    <div className="textarea-game__editor">
      <div className="textarea-game__css">
        <p className="textarea-game__text"> {"#dish {"}</p>
        <p className="textarea-game__text textarea-game__text-display-flex">
          display: flex;
        </p>

        <div className="textarea-game__line-numbers">
          <p className="textarea-game__number">1</p>
          <p className="textarea-game__number">2</p>
          <p className="textarea-game__number">3</p>
          <p className="textarea-game__number">4</p>
          <p className="textarea-game__number">5</p>
          <p className="textarea-game__number">6</p>
        </div>

        <textarea
          ref={inputRef}
          cols="39"
          rows={rows}
          className="textarea-game__code"
          spellCheck="false"
          placeholder="align-items: baseline;"
          onChange={handleChange}
          {...props}
        ></textarea>
        <p className="textarea-game__text-end">{"}"}</p>
      </div>
    </div>
  );
}
