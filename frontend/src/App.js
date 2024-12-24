import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import QuestionList from './components/QuestionList';  // 예시로 질문 목록 컴포넌트 추가
import QuestionDetail from './components/QuestionDetail';  // 질문 상세 컴포넌트 추가
import Login from './components/Login';  // 로그인 페이지 컴포넌트 추가
import { AuthProvider } from './context/AuthContext';  // AuthContext 추가

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <div>
                    <Routes>
                        <Route path="/" element={<QuestionList />} />
                        <Route path="/question/detail/:id" element={<QuestionDetail />} />
                        <Route path="/login" element={<Login />} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
};

export default App;