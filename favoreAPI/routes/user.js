var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");
var Post = require('../models/post');


router.post('/updateProfile',(req,res,next)=>{
  var path = "public/images/"+req.body.id+".jpg";
  var stat = uploadImage(req,res,path);
  if(stat==false){
    res.json({success:false,msg:"Timeout mate!",error:err});
    return;
  }else{
    res.json({success:true,msg:"Updated your profile pic!"});
  }
});

router.post('/getuserposts/:usr_id',function(req,res,next){
    var uid = req.params.usr_id;
    Post.find({posterId:uid},function(err,posts){
	if (err){
	    res.json({success:false,msg:"unable to fetch posts"});
	}else{
	    res.json({success:true,post:posts});
	}
});
});

router.post('/postimg/',function(req,res,next){
  var path = "/images/"+req.files.file.name;
  var stat = uploadImage(req,res,path);
  if(stat==false){
    res.json({success:false,msg:"Timeout mate!",error:err});
    return;
  }
  var newPost = new Post({
    location:req.body.location,
    favors:req.body.favors,
    age:req.body.age,
    date:Number(req.body.date),
    posterId:req.body.posterId,
    isImage:true,
    imagePath: path
  });

  newPost.save(function(err,suc){
    if (err){
	res.json({success:false,msg:"Unable to save the post"});
	console.log(err);
    }
      else{
	  console.log(newPost);
        User.findOne({id:req.body.posterId},function(err,usr){
          if (err){
              res.json({success:false,msg:"Some kind of error"});
	      console.log(err);
          } if (usr == null){
            res.json({success:false,msg:"User not found"})
          } else{
            var usr_posts = usr.posts;
            usr_posts.push(suc.id);
            User.update({id:usr.id},{$set:{posts:usr_posts}},function(err,out){
              if (err){
                  res.json({success:false,msg:"Some kind of error"});
              }
              else res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
            })
          }
        });
    }
  });
});

function uploadImage(req,res,path){
  // Images available at imagepath/postId.jpg
  console.log(req.files);
  var img = req.files.file;
  if (!req.files){
    res.json({success:false,msg:"No image to upload bch!"});
    return false;
  }
  else {
  img.mv(path,function(err){
    if (err){
      res.json({success:false,msg:"Failed to upload the file",error:err});
      return false;
    }
    else  return true;
  });
}
}
router.post("/editUser/",function(req,res,next){
  var user_details = req.body;
  var path = "public/images/"+user_details.id+".jpg";
  console.log(path);
  var stat = uploadImage(req,res,path);
  if (stat == false){
    res.json({success:false,msg:"Time out!"});
    return;
  }
  var path="/images/"+user_details.id+".jpg";
  console.log(user_details);
  User.update({id:user_details.id},{$set:{
    fname:user_details.fname,
    lname:user_details.lname,
    dname:user_details.dname,
    phone:user_details.phone,
    age:user_details.age,
    bio:user_details.bio,
    profileImg:path
  }},function(err,out){
    if (err){
      res.json({success:false,msg:"Some kind of error"});
    }
    else{
      res.json({success:true,msg:out})
    }
  })
});


router.post('/posttext/',function(req,res,next){
    // Sending the post
    console.log(req.body);
  var newPost = new Post({
    postText:req.body.postText,
    location:req.body.location,
    favors:req.body.favors,
    age:req.body.age,
    date:req.body.date,
    posterId:req.body.posterId,
    isImage:false
  });
  newPost.save(function(err,suc){
    if (err){
      res.json({success:false});
    }
    else{
        User.findOne({id:req.body.posterId},function(err,usr){
          if (err){
            res.json({success:false,msg:"Some kind of error"});
          } if (usr == null){
            res.json({success:false,msg:"User not found"})
          } else{
            var usr_posts = usr.posts;
            usr_posts.push(suc.id);
            User.update({id:usr.id},{$set:{posts:usr_posts}},function(err,out){
              if (err){
                  res.json({success:false,msg:"Some kind of error"});
              }
              else res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
            })
          }
        });
    }
  });
});

router.get('/fetchPosts/',function(req,res,next) {
  /*
  usage: localhost:3000/user/fetchPosts?lon=xx&lat=xx&uid=xx
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
		            res.json({success:false,msg:"Could not get the results"});
            }
	    //Now get the the posts of the users
	      User.findOne({id:req.query.uid},function(err,usr){
		    if (err){
		               res.json({success:false,msg:"could not get the user info"});
		    }
		    //Now fetch the posts of the given user
        frnds=usr.friends;
		    Post.find({posterId:{$in:frnds}},function(err,friend_post){
		        if (err){
			           res.json({success:false,msg:"Unable to fetch the friends"});
		             }
		    if (frnds.length == 0){
			       res.json({success:false,msg:"There are no friends"});
             return;
		    }
        var posts = [];
        for (var i = 0; i < frnds.length; ++i){
             var temp = frnds[i];
             var post = new Post({
                  postText:temp.postText,
                  location:temp.location,
                  favors:temp.favors,
                  id:temp.id,
                  age:temp.age,
                  date:temp.date
              });
              post.computeAge();
              posts.push(post);
        }
		    for (var i = 0; i < results.length; ++i){
			       var temp = results[i];
			       var post = new Post({
			            postText:temp.postText,
			            location:temp.location,
			            favors:temp.favors,
			            id:temp.id,
			            age:temp.age,
			            date:temp.date
        			});
			        post.computeAge();
			        posts.push(post);
		    }
		    res.json({success:"true",posts:posts})
		});
  });
})
});

router.get('/getUserDetails/:user_id',function(req,res,next) {
  /*
  usage: localhost:3000/<usr-name>
  */
  var user_id = req.params.user_id;
  User.find({id:user_id},function(err,usr){
    if (err){
      req.json({success:false,message: "Some kind of error!"});
    }
    if (usr == null){
        res.json({success:false,msg:"No such user"});
    }
    else res.json({success:true,user:usr});
  });
})

router.get('/getAllUsers',function(req,res,next){
  /*
    usage:localhost:3000/
    Returns all the uses in the collection
  */
  User.find(function(err,usrs){
    if (err) throw err;
    else res.json({"usr":usrs});
  })
});

router.get('/removePost/:post_id',function(req,res,next){
  /*
    usage:localhost:3000/<post+id>
    Removes the post and returns the ack
  */
  var post_id = req.params.post_id;
  Post.findOne({id:post_id},function(err,post){
    if (err){
      res.json({success:false,msg:"Some kind of error!"});
    }
    else if (post==null){
      res.json({success:false,msg:"No such post"});
    }
    else{
      var usr_id = post.posterId;
      User.findOne({id:usr_id},function(err,user){
        if (err){
          res.json({success:false,msg:"Some kind of error!"});
        }
        else if (post==null){
          res.json({success:false,msg:"No such user"});
        }
        else{
          var post_list = user.posts;
          post_list.splice(post_list.indexOf(post_id),1);
          User.update({id:user.id},{$set:{posts:post_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error!"});
            }else{
              Post.remove({id:post_id},function(err,msg){
                if (err) throw err;
                else{
                  res.json({success:true,msg:msg});
                }
              })
            }
          })
        }
      });
    }
  });
});



router.get("/favore/:post_id",function(req,res,next){
  var post_id = req.params.post_id;
  Post.find({id:post_id},function(err,pst){
    if (err){
      res.json({success:"False",msg:"Some kind of error"})
    }
    else if (pst == null){
      res.json({success:"False",msg:"No such post"})
    }
    else {
    var post = new Post({
      postText:pst.postText,
      id:pst.id,
      location:pst.location,
      favors:pst.favors,
      age:pst.age,
      date:pst.date
    });
    post.favored();
    Post.update({"_id":post_id},post,function(err,result){
      if (err){
        res.json({success:false,msg:"some kind of error"})
      }else{
        res.json({success:true,res:result})
      }
    });
  }
  })

});

router.get("/addFriend/",function(req,res,next){
  /*
    usage: localhost:3000/user/fetchPosts?usr=xx&frnd=xx
  */
  var user_id = req.query.usr;
  var frnd_id = req.query.frnd;
  if (user_id == frnd_id){
    res.json({success:false,msg:"Same user dumbo"});
  }
  else{
  // Check if the frind exists
  User.findOne({id:frnd_id},function(err,user){
    if (err){
      res.json({success:false,msg:"Some kind of the error!"});
    }
    else if (user==null){
      res.json({success:false,msg:"No such frind"})
    }
    else{
      User.findOne({id:user_id},function(err,ur){
        if (err){
          res.json({success:false,msg:"Some kind of the error!"});
        }else if (ur.friends.indexOf(frnd_id) != -1) {
          res.json({success:false,msg:"Already a friend"});
        }else{
          var friends_list = ur.friends;
          friends_list.push(frnd_id);
          User.update({id:user_id},{$set:{friends:friends_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error"});
            }
            else res.json({success:true,msg:out});
          });
        }
      });
    }
  });
}
});

router.get("/removeFriend/",function(req,res,next){
  var user_id = req.query.usr;
  var frnd_id = req.query.frnd;
  if (user_id == frnd_id){
    res.json({success:false,msg:"Same user dumbo"});
  }
  else{
  // Check if the frind exists
  User.findOne({id:frnd_id},function(err,user){
    if (err){
      res.json({success:false,msg:"Some kind of the error!"});
    }
    else if (user==null){
      res.json({success:false,msg:"No such friend"})
    }
    else{
      User.findOne({id:user_id},function(err,ur){
        if (err){
          res.json({success:false,msg:"Some kind of the error!"});
        }else if (ur.friends.indexOf(frnd_id) == -1) {
          res.json({success:false,msg:"Not a friend"});
        }else{
          var friends_list = ur.friends;

          friends_list.splice(friends_list.indexOf(frnd_id,1));
          console.log(friends_list);
          User.update({id:user_id},{$set:{friends:friends_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error"});
            }
            else res.json({success:true,msg:out});
          });
        }
      });
    }
  });
}
});

router.get("/getFriendList/:user_id",function(req,res,next){
  User.findOne({id:req.params.user_id},function(err,user){
    if(err){
      res.json({success:false,msg:"some kind of error"});
    }else{
      res.json({success:true,friends:user.friends});
    }
  });
});

router.get("/getFriendList/:user_id",function(req,res,next){
  User.remove({id:req.params.user_id},function(err,out){
    if(err){
      res.json({success:false,msg:"some kind of error"});
    }else{
      res.json({success:true,msg:"Successfully deleted user"});
    }
  })

});

module.exports = router;
