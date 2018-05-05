var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");


router.post('/signin/', function(req, res, next) {
  User.findOne({
    username:req.body.username
  }, function(err,user){
    if (err) throw err;
    if (!user){
      res.status(401).send({success: false, msg: 'Authentication failed. User not found'});
    }else{
      user.comparePassword(req.body.password, function(err, isMatch){
        if (isMatch && !err){
          var token = jwt.sign(user.toObject(), config.secret);
          res.json({success:true, token:'JWT '+token, user:user});
        }else{

          res.status(401).send({success:false, msg:'Authentication failed. Wrong password'});
        }
      })
    }
  });
});



router.post('/signup/',function(req,res,next){
  if (!req.body.username || !req.body.password){
    res.json({success:false, msg:'Please fill up stuff'});
  }else{
      User.findOne({username:req.body.username},function(err,usr){
	  if (err){
	      res.json({success:false,msg:"Some kind of error"});
	  }
	  else if (usr != null){
	      res.json({success:false,msg:"User already exists"});
	  }else{
	      var newUser = new User({
		  username: req.body.username,
		  password:req.body.password
	      });
	      newUser.save(function(err,success){
		  if(err){
		      res.json({success: false, msg:'Username already exists'});
		  }
		  res.json({success:true,msg:'Successfully created new user'});
	      })
	  }});
  }
});



module.exports = router;
