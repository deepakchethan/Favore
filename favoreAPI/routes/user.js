var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");
var Post = require('../models/post');

router.post('/post/',function(req,res,next){
  console.log(req.body);
  var newPost = new Post({
    post:req.body.post,
    location:req.body.location,
    favors:req.body.favors,
    age:req.body.age,
    date:req.body.date
  });
  newPost.save(function(err,suc){
    if (err){
      res.json({success:false});
    }
    res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
  });
});

router.get('/fetchPosts/',function(req,res,next) {
  /*
  usage: localhost:3000/user/fetchPosts?lon=xx&lat=xx
  */
  Post.aggregate(
        [
            {
                '$geoNear': {
                    'near': [parseFloat(req.query.lon),parseFloat(req.query.lat)],
                    'spherical': true,
                    'distanceField': 'dist',
                    'maxDistance': 1000
                }
            }
        ],
        function(err, results) {
            // do what you want with the results here
            if (err){
              res.json({"msg":"ding dong bell","err":err});
            }
            res.json(results)
        }
    )
});

router.get('/getUserDetails/:user_name',function(req,res,next) {
  /*
  usage: localhost:3000/<usr-name>
  */
  var user_name = req.params.user_name;
  Post.findOne(function(err,usr){
    if (err){
      req.json({success:false,message: "No user found!"});
    }
    res.json({success:true,user:usr});
  });
})

module.exports = router;
