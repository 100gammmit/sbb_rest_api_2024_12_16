import axios from "axios";

const API_URL = "http://localhost:8080/answers";

// 답변 생성
export const createAnswer = (questionId, answerData) => {
    return axios.post(`${API_URL}/create/${questionId}`, answerData);
};

// 답변 수정
export const modifyAnswer = (id, answerData) => {
    return axios.post(`${API_URL}/modify/${id}`, answerData);
};

// 답변 삭제
export const deleteAnswer = (id) => {
    return axios.get(`${API_URL}/delete/${id}`);
};

// 답변에 투표
export const voteAnswer = (id, questionId) => {
    return axios.get(`${API_URL}/vote/${id}`, { params: { questionId } });
};