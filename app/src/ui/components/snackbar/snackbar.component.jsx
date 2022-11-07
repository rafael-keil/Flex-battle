import { useEffect } from "react";
import { CLOSE_ICON } from "../../../assets";
import { DefaultClose } from "../defaultClose/defaultClose.component";
import "./snackbar.style.css";

export function Snackbar({ text, visibility, handleClose, type }) {
  useEffect(() => {
    setTimeout(() => {
      handleClose();
    }, 3000);
  }, [visibility]);

  if (visibility) {
    return (
      <div className={`snackbar snackbar__${type}`}>
        <p>{text}</p>
        <DefaultClose handleClose={handleClose} img={CLOSE_ICON} />
      </div>
    );
  }

  return null;
}
