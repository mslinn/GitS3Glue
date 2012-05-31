# AWS S3 access from Tomcat running on Heroku #

## To Run ##

    git clone git@github.com:mslinn/HerokuTomcatAwsS3.git
    git remote add heroku git@heroku.com:sharp-cloud-8613.git # use your Heroku app name here


Define two environment variables to hold your AWS access key and your AWS secret key:

    heroku config:add accessKey=34poslkflskeflsekjfl
    heroku config:add secretKey=asdfoif3r3wfw3wgagawgawgawgw3taw3tatefef

Deploy the project to Heroku:

    git push heroku master

The web app should now be up and running on Heroku. Open it in your browser with:

    heroku open

## Use Case ##
 1. Using GitHub and BitBucket for source control
 2. Hosting content on AWS S3 (CDN)
 3. S3 serves static web site
 4. Commits to GitHub / BitBucket should cause changed files to be copied to AWS S3. Remember that S3 is
passive and cannot pull, and this Heroku app should not poll for changes. A post-receive hook or service
needs to run on GitHub / BitBucket to push this Heroku app, which propagates changes to AWS S3.

## BitBucket POST Service ##
Each time files are pushed to BitBucket, a POST can originate from the repo and can go a designated URL.
For the details on the services included with Bitbucket, check out [BitBucket services](https://confluence.atlassian.com/display/BITBUCKET/Managing+bitbucket+Services)
and [POST service](https://confluence.atlassian.com/display/BITBUCKET/Setting+Up+the+bitbucket+POST+Service).

`fromBitBucket.jsp` does the following:

 1. Accepts a POST in JSON format from BitBucket describing the commit.
 2. Verifies the POST to be a result of a valid commit from BB.
 3. Reads a zip from BitBucket containing the committed files.
 4. Unpacks the zip and pushes content files to AWS S3.
