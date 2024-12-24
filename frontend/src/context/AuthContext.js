import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';
import { Navigate } from 'react-router-dom';

// Context 생성
const AuthContext = createContext();

// AuthContext를 사용하기 위한 hook
export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    // 컴포넌트가 마운트될 때 로그인 상태 확인
    useEffect(() => {
        axios.get('http://localhost:8080/user/info') // 로그인 상태 체크
            .then(response => {
                setUser(response.data); // 사용자 정보 업데이트
            })
            .catch(() => {
                setUser(null); // 로그인되지 않은 상태
            });
    }, []);

    // 로그인 함수
    const login = (username, password) => {
        return axios.post('http://localhost:8080/user/login', { username, password }) // 로그인 요청
            .then(response => {
                setUser(response.data); // 로그인 성공 시 사용자 정보 설정
            });
    };

    // 로그아웃 함수
    const logout = () => {
        return axios.post('http://localhost:8080/user/logout') // 로그아웃 요청
            .then(() => {
                setUser(null); // 사용자 정보 초기화
                Navigate('/');
            });
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};