import React, { useState } from "react";

const AnswerForm = ({ questionId, onSubmit }) => {
    const [content, setContent] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        const answerData = { content };
        onSubmit(answerData);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <textarea
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                />
            </div>
            <button type="submit">제출</button>
        </form>
    );
};

export default AnswerForm;