const express = require('express');
const path = require('path');
const proxy = require('express-http-proxy');

const app = express();

app.use(express.static('dist'));

app.use('/', proxy('http://localhost:8080'));

app.get('*',(req, res) => {
    res.sendFile(path.join(__dirname,'dist','index.html'));
});

const port = process.env.PORT || 3001;
app.listen(port, () => console.log('Listening on http://localhost:' + port));
