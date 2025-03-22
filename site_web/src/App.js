import React from "react";
import Navbar from "./navbar";
import Login from "./pages/login";
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import Home from "./pages/home";

function App() {
  return (
      <Router>
        <Navbar/>
        <Routes>

          <Route path='/login' element={<Login/>}/>
            <Route path='/' element={<Home/>}/>

        </Routes>
      </Router>
  )
}
export default App;