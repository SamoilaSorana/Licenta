import React from "react";
import Navbar from "./navbar";
import Login from "./pages/login";
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import Home from "./pages/home";
import Register from "./pages/register";
import Lectures from "./pages/lectures";
import LecturePage from "./pages/lecturePage";
import TestPage from "./pages/testpage";
import ProfilePage from "./pages/ProfilePage";
import GradedTestsPage from "./pages/GradedTestsPage";



function App() {
  return (
      <Router>
        <Navbar/>
        <Routes>

          <Route path='/login' element={<Login/>}/>
            <Route path='/' element={<Home/>}/>
            <Route path='/register' element={<Register/>}/>
            <Route path='/lectures' element={<Lectures/>}/>
            <Route path="/lecture/:id" element={<LecturePage />} />
            <Route path="/lecture/:id/test" element={<TestPage />} />
            <Route path='/profile' element={<ProfilePage/>}/>
            <Route path="/graded-tests" element={<GradedTestsPage />} />


        </Routes>
      </Router>
  )
}
export default App;