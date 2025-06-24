//config file.env depending on environment var / NODE_ENV=prod node app.js


require('dotenv').config({
    path: '.env.development'
  });


console.log("S3_ENDPOINT: " + process.env.S3_ENDPOINT)



console.log("region : "+ process.env.S3_REGION)
console.log("NODE_ENV: "+ process.env.NODE_ENV)
console.log("NODE_ENV: fin")

var createError = require('http-errors');
var express = require('express');

var storageRoutes = require('./routes/storageRoutes');
var app = express();





//body REQUEST format = json
app.use(express.json());
// habilitamos peticiones con datos tipo application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: false }));


//config path API
app.use('/api/v1/fileStorage', storageRoutes);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});


module.exports = app;
