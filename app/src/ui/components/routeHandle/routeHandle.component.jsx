import { Redirect, Route, Switch } from "react-router"
import {
  BattleMenu,
  Login,
  Menu,
  Training,
  OwnerRoom,
  OpponentRoom,
  Ranking,
} from "../../screens"
import { PrivateRoute } from "./components"
import { PATH } from "../../../constants"
import { useGlobalUser } from "../../../context"

export function RouteHandle() {
  const [user] = useGlobalUser()

  const isLoggedIn = !!user
  return (
    <Switch>
      {!isLoggedIn && (
        <Route path={PATH.LOGIN} exact>
          <Login />
        </Route>
      )}
      <PrivateRoute path={PATH.MENU} exact>
        <Menu />
      </PrivateRoute>
      <PrivateRoute path={PATH.TRAINING} exact>
        <Training />
      </PrivateRoute>
      <PrivateRoute path={PATH.BATTLE_MENU} exact>
        <BattleMenu />
      </PrivateRoute>
      <PrivateRoute path={PATH.BATTLE_CREATE} exact>
        <OwnerRoom />
      </PrivateRoute>
      <PrivateRoute path={PATH.BATTLE_ENTER} exact>
        <OpponentRoom />
      </PrivateRoute>
      <PrivateRoute path={PATH.RANKING} exact>
        <Ranking />
      </PrivateRoute>
      <Route path="/">
        <Redirect to="/" />
      </Route>
    </Switch>
  )
}
