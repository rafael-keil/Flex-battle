import { Redirect, Route } from "react-router";
import { useGlobalUser } from "../../../../../context";
import { PATH } from "../../../../../constants";

export function PrivateRoute({ path, children }) {
  const [user] = useGlobalUser();

  if (!user) {
    return (
      <Redirect
        to={{
          pathname: PATH.LOGIN,
          state: { invalidToken: "invalid" },
        }}
      />
    );
  }

  return (
    <Route path={path} exact>
      {children}
    </Route>
  );
}
