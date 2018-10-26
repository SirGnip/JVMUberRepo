# JavaFX uberrepo

# thoughts
do an uber repo for my JavaFX code (games and UI utils). But, acknowledge that the repo may contain some sub artifacts that could be used by other code external to the uber repo (if I write generic Java string utils, have things structured that I could build a .jar of just the string utils instead of the whole JavaFX uber-repo.  Just because everything is in the uber-repo doesn’t mean it has to be distributed that way.

# details

java/
java/uber or repo
java/sandbox
java/learning

get everything building
- x javafxutils
- x clipdashboard
- fluxflihgt
- javafluxflight

don't commit todo.md files. have one in each sub project?
keep all intellij stuff separate for now
keep all library building separate for now

java/ <uber repo>
java/learning


cd uberrepo
git remote add -f cliprepo /c/_other/PLAY/MYCLIP/
git merge cliprepo/master --allow-unrelated-histories


git rebase cliprepo/master???
git remote rm cliprepo



mkdir uberrepo
cd uberrepo
git init .
create uber README.md (mostly empty) and add/commit it

git remote add -f cliprepo /c/_other/PLAY/MYCLIP/
git rebase cliprepo/master
git remote rm cliprepo
mkdir clipdash
git mv 





* ba8367f (HEAD -> master) Inital uber repo commit
* 9a0b53b (tag: 0.2, fxlibrepo/master) Show text for Labeled objects in scene graph dump
* 8bb2672 (tag: 0.1) Initial commit


* c7ebbaa Show text for Labeled objects in scene graph dump
* a432689 Initial commit


