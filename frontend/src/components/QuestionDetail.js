import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Pagination, Button } from 'react-bootstrap';
import Navbar from './Navbar';

const QuestionDetail = () => {
    const { id } = useParams();
    const [question, setQuestion] = useState(null);
    const [answers, setAnswers] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [content, setContent] = useState('');
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        // Fetch question details and answers from the backend
        axios.get(`http://localhost:8080/questions/detail/${id}`)
            .then(response => {
                // Destructuring the response data
                const { questionDTO, answerDTOPage } = response.data;
                setQuestion(questionDTO);
                setAnswers(answerDTOPage.content);  // Extracting answers from the paginated list
                setTotalPages(answerDTOPage.totalPages);  // Setting the total number of pages for pagination
            })
            .catch(error => {
                console.error('Error fetching question detail:', error);
            });
    }, [id]);

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setCurrentPage(newPage);
            // Optionally, you can make another API call to fetch answers for the new page
        }
    };

    const handleAnswerSubmit = (e) => {
        e.preventDefault();
        axios.post(`http://localhost:8080/answer/create/${id}`, { content })
            .then(response => {
                // Handle the response, e.g., refresh the answers list
                setAnswers([...answers, response.data]);
                setContent(''); // Clear the answer form
            })
            .catch(error => {
                console.error('Error posting answer:', error);
            });
    };

    return (
        <div>
            <Navbar isAuthenticated={isAuthenticated} />
            {question && (
                <div className="container my-3">
                    <h2 className="border-bottom py-2">{question.subject}</h2>
                    <div className="card my-3">
                        <div className="card-body">
                            <div className="card-text">{question.content}</div>
                            <div className="d-flex justify-content-end">
                                {question.lastModifiedDate && (
                                    <div className="badge bg-light text-dark p-2 text-start mx-3">
                                        <div className="mb-2">Modified At</div>
                                        <div>{new Date(question.lastModifiedDate).toLocaleString()}</div>
                                    </div>
                                )}
                                <div className="badge bg-light text-dark p-2 text-start">
                                    <div className="mb-2">{question.author}</div>
                                    <div>{new Date(question.createdDate).toLocaleString()}</div>
                                </div>
                            </div>
                            <div className="my-3">
                                <Button variant="outline-secondary" onClick={() => alert('추천')}>
                                    추천 <span className="badge rounded-pill bg-success">{question.voterCount}</span>
                                </Button>
                                <Button variant="outline-secondary" onClick={() => alert('수정')}>
                                    수정
                                </Button>
                                <Button variant="outline-secondary" onClick={() => alert('삭제')}>
                                    삭제
                                </Button>
                            </div>
                        </div>
                    </div>

                    <h5 className="border-bottom my-3 py-2">
                        {answers.length}개의 답변이 있습니다.
                    </h5>

                    {answers.map((answer) => (
                        <div className="card my-3" key={answer.id}>
                            <div className="card-body">
                                <div className="card-text">{answer.content}</div>
                                <div className="d-flex justify-content-end">
                                    {answer.lastModifiedDate && (
                                        <div className="badge bg-light text-dark p-2 text-start mx-3">
                                            <div className="mb-2">Modified At</div>
                                            <div>{new Date(answer.lastModifiedDate).toLocaleString()}</div>
                                        </div>
                                    )}
                                    <div className="badge bg-light text-dark p-2 text-start">
                                        <div className="mb-2">{answer.author}</div>
                                        <div>{new Date(answer.createdDate).toLocaleString()}</div>
                                    </div>
                                </div>
                                <div className="my-3">
                                    <Button variant="outline-secondary" onClick={() => alert('추천')}>
                                        추천 <span className="badge rounded-pill bg-success">{answer.voterCount}</span>
                                    </Button>
                                    <Button variant="outline-secondary" onClick={() => alert('수정')}>
                                        수정
                                    </Button>
                                    <Button variant="outline-secondary" onClick={() => alert('삭제')}>
                                        삭제
                                    </Button>
                                </div>
                            </div>
                        </div>
                    ))}

                    {totalPages > 0 && (
                        <Pagination className="justify-content-center">
                            <Pagination.Prev
                                onClick={() => handlePageChange(currentPage - 1)}
                                disabled={currentPage === 0}
                            />
                            {Array.from({ length: totalPages }).map((_, index) => (
                                <Pagination.Item
                                    key={index}
                                    active={index === currentPage}
                                    onClick={() => handlePageChange(index)}
                                >
                                    {index + 1}
                                </Pagination.Item>
                            ))}
                            <Pagination.Next
                                onClick={() => handlePageChange(currentPage + 1)}
                                disabled={currentPage === totalPages - 1}
                            />
                        </Pagination>
                    )}

                    <form onSubmit={handleAnswerSubmit} className="my-3">
                        <textarea
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            className="form-control"
                            rows="5"
                            placeholder="답변을 입력하세요"
                            disabled={!isAuthenticated}
                        />
                        <Button type="submit" className="btn btn-primary my-2" disabled={!isAuthenticated}>
                            답변 등록
                        </Button>
                    </form>
                </div>
            )}
        </div>
    );
};

export default QuestionDetail;