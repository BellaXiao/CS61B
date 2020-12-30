# CS61B
2018 Spring
https://sp18.datastructur.es/

start: 2020/10/3


## Learning Memo
run java in sublime3 text: cmd+b (build system:java-bai)


## Order & Version Change
<p>version -- 19sp
vid1(10/3 done)/vid2（10/3 done)
guide(10/3 done)/reading(10/3 done)
HW0(10/4 done) 
lab1b (10/4 done)
lab1 (10/4 done)
Disc 1 (10/4 done)
vid 1/25 (10/4 done)
reading 1.2 (10/4 done)
Proj 0(10/8 - before "Drawing the Background"）
(10/11 done）</p>

version -- change to 18sp(https://sp18.datastructur.es/)
also tracker in Notion app.




## run java class in cmd
proj0 apple$ java -cp ./examples: StdDrawDemo
-- (-cp means set classpath, the default value if current directory)

## Note:
Everytime we updated java file, we need to use javac to complie again to get new class file to show the update result.



## git memo:
(https://sp18.datastructur.es/materials/guides/using-git.html) -- Amazing tutorial!!!!

3 main cmd:
	* git add []
	* git commit -m "[]"
	* git push origin main


self:
* for a new one, can build a repository in github websidte, then connect remote repository with local one, clone to local first (Note, you can also choose to only use a local git repository)

cd to coding file:
* git clone url(clone the project in github to local) 
—-(if we already have some content in remote git)

* git remote add origin “url under codes button”
—-(if we don’t have remote files that don’t exist locally;
“origin” above can be rename, and the push command below would change that ‘origin’to the new name)

* git remote rm origin(or other name)
--(to remove that remote connection you build before)

* git init (at certain path) (cd to cs61a)
--(Note:In order to begin, you must initialize a Git repository by typing the following command into your terminal while in the directory whose history you want to store in a local repository. )

* git status
git add . (git add ./lab01)
git status (used to check what files are added to be tracked and what are untracked)

* git commit -m “string”
--(Note:Your message should be descriptive and explain what changes your commit makes to your code. You may want to quickly describe bug fixes, implemented classes, etc. so that your messages are helpful later when looking through your commit log.)

* git push origin master 
--(Used for push changes to remote repository)
--(origin can be changed to any name you rename above ) (and the default branch name master has changed to main branch starting from 2020) —> git push origin(bx) main

* rm -rf .git (delete a git repository locally after accidentally use git init on that file)

* git checkout
git checkout 9f955d85359fc8e4504d7220f13fad34f8f2c62b ./recipes/tofu
--important!!!: Make sure to specify a file (or directory) when you use checkout. 



-- (Get projects and assignments skeleton from CS61B official git)
* git pull skeleton master
* git pull skeleton master --allow-unrelated-histories
--(skeleton is the remote repository for CS61B course official github)
* May appear below results:
* "Please enter a commit message to explain why this merge is necessary,especially if it merges an updated upstream into a topic branch."
* It's not a Git error message, it's the editor as git uses your default editor.

* To solve this:
	press "i" (i for insert)
	write your merge message
	press "esc" (escape)
	write ":wq" (write & quit)
	then press enter


* https://bitmingw.com/2018/08/19/git-submodule-tutorial/
-- git add submodule

