import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Pagination, Button, InputGroup, FormControl } from 'react-bootstrap';
import Navbar from './Navbar';
import { useAuth } from '../context/AuthContext'; // useAuth 훅 임포트

const QuestionList = () => {
    const { user, logout } = useAuth(); // useAuth 훅을 통해 user와 logout 가져오기
    const [questions, setQuestions] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [kw, setKw] = useState(''); // 검색어
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        axios.get(`http://localhost:8080/questions/list?page=${currentPage}&size=10&kw=${kw}`)
            .then(response => {
                setQuestions(response.data.content);
                setTotalPages(response.data.totalPages);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching questions:', error);
                setLoading(false);
            });
    }, [currentPage, kw]);

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setCurrentPage(newPage);
        }
    };

    const handleSearch = () => {
        setCurrentPage(0);  // 검색시 첫 페이지로 돌아가기
    };

    const getPaginationRange = () => {
        const range = [];
        const start = Math.max(0, currentPage - 5);
        const end = Math.min(totalPages - 1, currentPage + 5);

        for (let i = start; i <= end; i++) {
            range.push(i);
        }
        return range;
    };

    const handleLogout = () => {
        logout(); // 로그아웃 처리
    };

    return (
        <div>
            <Navbar isAuthenticated={!!user} onLogout={handleLogout} /> {/* Navbar에 로그인 상태 전달 */}
            <div className="container my-3">
                <div className="row my-3">
                    <div className="col-6">
                        <a href="/question/create" className="btn btn-primary">질문 등록하기</a>
                    </div>
                    <div className="col-6">
                        <InputGroup>
                            <FormControl
                                type="text"
                                id="search_kw"
                                value={kw}
                                onChange={(e) => setKw(e.target.value)}
                                placeholder="검색어"
                            />
                            <Button variant="outline-secondary" id="btn_search" onClick={handleSearch}>찾기</Button>
                        </InputGroup>
                    </div>
                </div>

                {loading && <p>Loading...</p>}

                {!loading && (
                    <>
                        <table className="table">
                            <thead className="table-dark">
                                <tr className="text-center">
                                    <th>번호</th>
                                    <th style={{ width: '50%' }}>제목</th>
                                    <th>글쓴이</th>
                                    <th>작성일시</th>
                                </tr>
                            </thead>
                            <tbody>
                                {questions.map((question, index) => (
                                    <tr className="text-center" key={question.id}>
                                        <td>{totalPages * 10 - currentPage * 10 - index}</td>
                                        <td className="text-start">
                                            <a href={`/question/detail/${question.id}`}>{question.subject}</a>
                                            {question.answerCount > 0 && (
                                                <span className="text-danger small ms-2">{question.answerCount}</span>
                                            )}
                                        </td>
                                        <td>{question.author}</td>
                                        <td>{new Date(question.createdDate).toLocaleString()}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>

                        {totalPages > 0 && (
                            <div>
                                <Pagination className="justify-content-center">
                                    <Pagination.Prev
                                        onClick={() => handlePageChange(currentPage - 1)}
                                        disabled={currentPage === 0}
                                    />
                                    {getPaginationRange().map((pageNum) => (
                                        <Pagination.Item
                                            key={pageNum}
                                            active={pageNum === currentPage}
                                            onClick={() => handlePageChange(pageNum)}
                                        >
                                            {pageNum + 1}
                                        </Pagination.Item>
                                    ))}
                                    <Pagination.Next
                                        onClick={() => handlePageChange(currentPage + 1)}
                                        disabled={currentPage === totalPages - 1}
                                    />
                                </Pagination>
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};

export default QuestionList;