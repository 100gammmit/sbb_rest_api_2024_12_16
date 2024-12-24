import React, { useState } from 'react';
import axios from 'axios';

const QuestionForm = ({ initialData = null, onSuccess }) => {
    const [subject, setSubject] = useState(initialData?.subject || '');
    const [content, setContent] = useState(initialData?.content || '');
    const [errors, setErrors] = useState({});
    
    // 에러 메시지를 처리하는 함수
    const handleError = (field, message) => {
        setErrors(prevErrors => ({
            ...prevErrors,
            [field]: message
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 요청할 데이터 준비
        const data = { subject, content };

        try {
            const response = await axios.post('http://localhost:8080/questions/create', data); // 질문 등록 API 호출
            if (response.status === 200) {
                onSuccess(response.data);  // 성공적으로 저장된 후 처리
            }
        } catch (error) {
            // 서버에서 에러가 발생한 경우
            if (error.response && error.response.data.errors) {
                const serverErrors = error.response.data.errors;
                serverErrors.forEach(err => handleError(err.field, err.defaultMessage));
            }
        }
    };

    return (
        <div className="container">
            <h5 className="my-3 border-bottom pb-2">{initialData ? '질문 수정' : '질문 등록'}</h5>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="subject" className="form-label">제목</label>
                    <input
                        type="text"
                        id="subject"
                        value={subject}
                        onChange={(e) => setSubject(e.target.value)}
                        className="form-control"
                        autoComplete="off"
                    />
                    {errors.subject && <div className="text-danger">{errors.subject}</div>}
                </div>
                <div className="mb-3">
                    <label htmlFor="content" className="form-label">내용</label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        className="form-control"
                        rows="10"
                    />
                    {errors.content && <div className="text-danger">{errors.content}</div>}
                </div>
                <button type="submit" className="btn btn-primary my-2">
                    {initialData ? '수정하기' : '저장하기'}
                </button>
            </form>
        </div>
    );
};

export default QuestionForm;