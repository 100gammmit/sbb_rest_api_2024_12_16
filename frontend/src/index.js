import 'bootstrap/dist/css/bootstrap.min.css'
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';

// 'root'라는 id를 가진 HTML 엘리먼트를 찾아서 React 앱을 마운트합니다.
const root = ReactDOM.createRoot(document.getElementById('root'));  // createRoot 사용
root.render(<App />);  // <App />를 렌더링