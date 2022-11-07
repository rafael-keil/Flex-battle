import "./header.style.css";
import { MenuButton, Title } from "../index";

export function Header({ img, menuButton, currentUrl, userUpdate }) {
  return (
    <header className="header__menu">
      {menuButton ? (
        <MenuButton currentUrl={currentUrl} userUpdate={userUpdate} />
      ) : null}

      <Title />

      {img ? (
        <img
          src={img}
          className="header__battle-icon"
          alt="ícone default de usuário"
        />
      ) : null}
    </header>
  );
}
