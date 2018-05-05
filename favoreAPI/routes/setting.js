var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var route = express.Router();
var User = require("../models/user");
var Post = require('../models/post');


route.post('/updateName',(req,res,next)=>{
  // Update the fname and lname
  var name = req.body;
  User.update({id:name.id},{$set:{fname:name.fname,lname:name.lname}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your name"})
    }else{
      res.json({success:true,msg:"Successfully updated your name"});
    }
  });
});

route.post('/updateDName',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{dname:name.dname}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your display name"})
    }else{
      res.json({success:true,msg:"Successfully updated your display name"});
    }
  });
});

route.post('/updateEmail',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{uname:name.uname}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your email"})
    }else{
      res.json({success:true,msg:"Successfully updated your email"});
    }
  });
});

route.post('/updatePass',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{password:name.password}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your password"})
    }else{
      res.json({success:true,msg:"Successfully updated your password"});
    }
  });
});

route.post('/updateAge',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{age:name.age}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your age"})
    }else{
      res.json({success:true,msg:"Successfully updated your age"});
    }
  });
});

route.post('/updatePhone',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{phone:name.phone}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your phone number"})
    }else{
      res.json({success:true,msg:"Successfully updated your phone number"});
    }
  });
});

route.post('/updateGender',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{gender:name.gender}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your gender"})
    }else{
      res.json({success:true,msg:"Successfully updated your gender"});
    }
  });
});

route.post('/updateBio',(req,res,next)=>{
  var name = req.body;
  User.update({id:name.id},{$set:{bio:name.bio}},function(err,res){
    if (err){
      res.json({success:false,msg:"failed to update your bio"})
    }else{
      res.json({success:true,msg:"Successfully updated your bio"});
    }
  });
});







module.exports = route;
