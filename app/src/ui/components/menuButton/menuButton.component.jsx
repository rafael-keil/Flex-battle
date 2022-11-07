import "./menuButton.style.css";
import { MENU_BUTTON } from "../../../assets";
import { useEffect, useState } from "react";
import { DefaultMenu } from "../../components";
import { useFbApi } from "../../../hooks";

export function MenuButton({ currentUrl, userUpdate }) {
  const [showOptions, setShowOptions] = useState(false);
  const [user, setUser] = useState();
  const fbApi = useFbApi();

  useEffect(() => {
    async function getUser() {
      try {
        const newUser = await fbApi.getUser();

        setUser(newUser);
      } catch {}
    }

    getUser();
  }, [fbApi, userUpdate]);

  function reverseState() {
    setShowOptions(!showOptions);
  }

  function checkButton() {
    if (showOptions === true) {
      return (
        <div className="menu-button__container">
          <DefaultMenu
            currentUrl={currentUrl}
            buttonStateCallback={reverseState}
            user={user}
          />
        </div>
      );
    }
    return null;
  }

  return (
    <>
      <button className="menu-button" onClick={reverseState}>
        <img src={MENU_BUTTON} alt="Botão de abrir menu" />
      </button>
      {checkButton()}
    </>
  );
}
