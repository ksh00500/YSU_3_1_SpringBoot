INSERT INTO POST (TITLE, CODE_CONTENT, LANGUAGE, NICKNAME, POST_TYPE, CREATED_AT) VALUES
('Java BubbleSort Review', 'for(int i=0;i<n-1;i++){for(int j=0;j<n-i-1;j++){if(arr[j]>arr[j+1]){int tmp=arr[j];arr[j]=arr[j+1];arr[j+1]=tmp;}}}', 'Java', 'hong123', 'tech', CURRENT_TIMESTAMP),
('Python dict usage question', 'd = {}\nfor item in items:\n    d[item] = d.get(item, 0) + 1', 'Python', 'kim456', 'question', CURRENT_TIMESTAMP),
('Junior dev resume advice', 'No experience - what to put in portfolio?', 'Java', 'park789', 'career', CURRENT_TIMESTAMP);

INSERT INTO COMMENT (POST_ID, NICKNAME, BODY, CREATED_AT) VALUES
(1, 'kim456', 'O(n^2) - why not Arrays.sort()?', CURRENT_TIMESTAMP),
(1, 'park789', 'Good example, thanks!', CURRENT_TIMESTAMP);

INSERT INTO SUGGEST (POST_ID, NICKNAME) VALUES
(1, 'kim456'),
(1, 'park789');
