import React from "react";
import "./index.css";
import Login from "./Login";
import App from "./App";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Box from "@material-ui/core/Box";
import { Typography } from "@material-ui/core";
import SignUp from "./SignUp";

function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {"Copyright "}
      fsoftengineer, {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

class AppRouter extends React.Component {
  render() {
    return (
      <div>
        <Router>
          <div>
            {/* React-router-dom V6 이후 Switch -> routes로 바뀜  Route 안에 element 사용 */}
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/" element={<App />} />
              <Route path="/signup" element={<SignUp />} />
            </Routes>
          </div>
          <Box mt={5}>
            <Copyright />
          </Box>
        </Router>
      </div>
    );
  }
}

export default AppRouter;
