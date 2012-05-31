AWS S3 access from Tomcat running on Heroku
===========================================

    git clone git@github.com:mslinn/HerokuTomcatAwsS3.git
    git remote add heroku git@heroku.com:sharp-cloud-8613.git # use your Heroku app name here


Define two environment variables to hold your AWS access key and your AWS secret key:

    heroku config:add accessKey=34poslkflskeflsekjfl
    heroku config:add secretKey=asdfoif3r3wfw3wgagawgawgawgw3taw3tatefef

Deploy the project to Heroku:

    git push heroku master

The web app should now be up and running on Heroku. Open it in your browser with:

    heroku open

