var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");
var Post = require('../models/post');


router.get('/fetchFollowersPost/',function(req,res,next){
  var posts = []
  User.findOne({id:req.query.uid},function(err,usr){
     if (err){
            res.json({success:false,msg:"could not get the user info"});
     }
     if (usr == null){
        res.json({success:false,msg:"No such user"});
      }
     var frnds=usr.friends;
     console.log(frnds);
     Post.aggregate([
        {
          '$match':{posterId:{'$in':frnds}}
        },
        {
            '$lookup':{
              'from':"users",
              'localField':"posterId",
              'foreignField':"id",
              'as':'user_details'
            }
        }
     ]
     ,function(err,results){
       if (err){
         res.json({"success":false,"msg":"Some kind of error!"});
         return;
       }
       if(results.length == 0){
         res.json({success:true,msg:"You have no friends or friends who post"});
         return;
       }
       for(var i = 0; i < results.length; ++i){
          var temp = results[i];
          var post =new Post({
            postText:temp.postText,
            location:temp.location,
            favors:temp.favors,
            id:temp.id,
            age:temp.age,
            poster:temp.user_details[0].username,
            posterProfile:temp.user_details[0].posterProfile,
            postImage:temp.postImage,
            isImage:temp.isImage,
            date:temp.date
          });
          pos.push(post);
        }
        res.json({"success":true,posts:pos})
     })
});
});

router.get('/fetchLocationPosts/',function(req,res,next) {
  /*
  usage: localhost:3000/user/fetchPostsfol?lon=xx&lat=xx
  */
  var pos=[];
  Post.aggregate(
        [
            {
                '$geoNear': {
                    'near': [parseFloat(req.query.lon),parseFloat(req.query.lat)],
                    'spherical': true,
                    'distanceField': 'dist',
                    'maxDistance': 1000
                }
            },
            {
                '$lookup':{
                  'from':"users",
                  'localField':"posterId",
                  'foreignField':"id",
                  'as':'user_details'
                }
            }
        ],
        function(err, results) {
            // do what you want with the results here
            if (err){
		            res.json({success:false,msg:"Could not get the results"});
                return;
            }

            // Now putting the posts got using the direct querying
            for(i = 0; i < results.length; ++i){
               var temp = results[i];
               var post = new Post({
                 postText:temp.postText,
                 location:temp.location,
                 favors:temp.favors,
                 id:temp.id,
                 age:temp.age,
                 poster:temp.user_details[0].username,
                 posterProfile:temp.user_details[0].posterProfile,
                 postImage:temp.postImage,
                 isImage:temp.isImage,
                 date:temp.date
               });
               pos.push(post);
		       }
           res.json({success:"true",posts:pos});
         }
	 );
});


module.exports = router;
