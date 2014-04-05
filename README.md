This is a cross-sbt reproduction test of sbt/sbt#641 to fix sbt/sbt#1091.

- sbt/sbt@482f9905235217c339934d8a7a560aae13b7f296 deletes a bunch of JARs to fix sbt/sbt#641.
- sbt/sbt@718fa9177254e6067cafdc1ee6a744ba5c325921 corrected Test jars classifier to tests for sbt/sbt#683.

We can roll back sbt/sbt@482f9905235217c339934d8a7a560aae13b7f296 without causing regression of sbt/sbt#641 since sbt/sbt@718fa9177254e6067cafdc1ee6a744ba5c325921 fixes the underlying problem.

## 0.12.1

### steps

```
echo "sbt.version=0.12.1" > project/build.properties
rm -r ivy
rm -r demo-repo
rm a/A.scala
rm b/B.scala
xsbt a/clean a/publish
cp changes/B.scala b/B.scala
xsbt b/clean b/compile
cp changes/A.scala a/A.scala
xsbt a/clean a/publish
sleep 3
xsbt b/clean b/compile
```

### result

```
$ xsbt b/clean b/compile
[info] Loading project definition from /Users/eugene/work/quick-test/sbt-641/project
[info] Set current project to b (in build file:/Users/eugene/work/quick-test/sbt-641/)
[success] Total time: 0 s, completed Apr 5, 2014 1:03:11 AM
[info] Updating {file:/Users/eugene/work/quick-test/sbt-641/}b...
[info] Resolving org.example#artifacta_2.10;1.0.0-SNAPSHOT ...
[info] downloading file:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.jar ...
[info]  [SUCCESSFUL ] org.example#artifacta_2.10;1.0.0-SNAPSHOT!artifacta_2.10.jar (4ms)
[warn]  [NOT FOUND  ] org.example#artifacta_2.10;1.0.0-SNAPSHOT!artifacta_2.10.jar (0ms)
[warn] ==== demo: tried
[warn]   file:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT-tests.jar
[warn]  ::::::::::::::::::::::::::::::::::::::::::::::
[warn]  ::              FAILED DOWNLOADS            ::
[warn]  :: ^ see resolution messages for details  ^ ::
[warn]  ::::::::::::::::::::::::::::::::::::::::::::::
[warn]  :: org.example#artifacta_2.10;1.0.0-SNAPSHOT!artifacta_2.10.jar
[warn]  ::::::::::::::::::::::::::::::::::::::::::::::
sbt.ResolveException: download failed: org.example#artifacta_2.10;1.0.0-SNAPSHOT!artifacta_2.10.jar
        at sbt.IvyActions$.sbt$IvyActions$$resolve(IvyActions.scala:214)
        at sbt.IvyActions$$anonfun$update$1.apply(IvyActions.scala:122)
        at sbt.IvyActions$$anonfun$update$1.apply(IvyActions.scala:121)
        at sbt.IvySbt$Module$$anonfun$withModule$1.apply(Ivy.scala:114)
        at sbt.IvySbt$Module$$anonfun$withModule$1.apply(Ivy.scala:114)
        at sbt.IvySbt$$anonfun$withIvy$1.apply(Ivy.scala:102)
        at sbt.IvySbt.liftedTree1$1(Ivy.scala:49)
        at sbt.IvySbt.action$1(Ivy.scala:49)
        at sbt.IvySbt$$anon$3.call(Ivy.scala:58)
```

### demo-repo


```
$ tree demo-repo 
demo-repo
└── org
    └── example
        └── artifacta_2.10
            └── 1.0.0-SNAPSHOT
                ├── artifacta_2.10-1.0.0-SNAPSHOT-javadoc.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT-javadoc.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT-javadoc.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT-test.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT-test.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT-test.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT.pom
                ├── artifacta_2.10-1.0.0-SNAPSHOT.pom.md5
                └── artifacta_2.10-1.0.0-SNAPSHOT.pom.sha1

4 directories, 15 files
```

### ivy cache

```
$ tree ivy/cache/cache/org.example 
ivy/cache/cache/org.example
└── artifacta_2.10
    ├── ivy-1.0.0-SNAPSHOT.xml
    ├── ivy-1.0.0-SNAPSHOT.xml.original
    ├── ivydata-1.0.0-SNAPSHOT.properties
    ├── jars
    │   └── artifacta_2.10-1.0.0-SNAPSHOT.jar
    └── srcs
        └── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar

3 directories, 5 files

$ cat ivy/cache/cache/org.example/artifacta_2.10/ivydata-1.0.0-SNAPSHOT.properties 
#ivy cached data file for org.example#artifacta_2.10;1.0.0-SNAPSHOT
#Sat Apr 05 01:03:11 PDT 2014
artifact\:artifacta_2.10\#pom.original\#pom\#-182976915.exists=true
artifact\:artifacta_2.10\#src\#jar\#11539150.is-local=false
artifact\:artifacta_2.10\#jar\#jar\#-1691211787.is-local=false
artifact\:artifacta_2.10\#pom.original\#pom\#-182976915.location=file\:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.pom
artifact\:ivy\#ivy\#xml\#684068265.is-local=false
artifact\:artifacta_2.10\#jar\#jar\#-1835539577.exists=true
resolver=demo
artifact\:ivy\#ivy\#xml\#684068265.location=file\:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.pom
artifact\:artifacta_2.10\#src\#jar\#11539150.exists=true
artifact\:artifacta_2.10\#pom.original\#pom\#-182976915.is-local=false
artifact\:artifacta_2.10\#jar\#jar\#-1835539577.is-local=false
artifact\:artifacta_2.10\#jar\#jar\#-1835539577.location=file\:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.jar
artifact\:artifacta_2.10\#jar\#jar\#-1691211787.exists=true
artifact.resolver=demo
artifact\:artifacta_2.10\#src\#jar\#11539150.location=file\:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT-sources.jar
artifact\:artifacta_2.10\#jar\#jar\#-1691211787.location=file\:/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.jar
artifact\:ivy\#ivy\#xml\#684068265.exists=true
```

## 0.13.5-SNAPSHOT

### steps

```
echo "sbt.version=0.13.5-SNAPSHOT" > project/build.properties
rm -r ivy
rm -r demo-repo
rm a/A.scala
rm b/B.scala
xsbt a/clean a/publish
cp changes/B.scala b/B.scala
xsbt b/clean b/compile
cp changes/A.scala a/A.scala
xsbt a/clean a/publish
sleep 3
xsbt b/clean b/compile
```

### result

```
$ xsbt b/clean b/compile
[info] Loading project definition from /Users/eugene/work/quick-test/sbt-641/project
[info] Set current project to sbt-641 (in build file:/Users/eugene/work/quick-test/sbt-641/)
[success] Total time: 0 s, completed Apr 5, 2014 1:08:19 AM
[info] Updating {file:/Users/eugene/work/quick-test/sbt-641/}b...
[info] Resolving org.fusesource.jansi#jansi;1.4 ...
[info] Done updating.
[info] Compiling 1 Scala source to /Users/eugene/work/quick-test/sbt-641/b/target/scala-2.10/classes...
[success] Total time: 2 s, completed Apr 5, 2014 1:08:21 AM
```

### demo-repo

```
$ tree demo-repo 
demo-repo
└── org
    └── example
        └── artifacta_2.10
            └── 1.0.0-SNAPSHOT
                ├── artifacta_2.10-1.0.0-SNAPSHOT-javadoc.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT-javadoc.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT-javadoc.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT-sources.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT-tests.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT-tests.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT-tests.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT.jar
                ├── artifacta_2.10-1.0.0-SNAPSHOT.jar.md5
                ├── artifacta_2.10-1.0.0-SNAPSHOT.jar.sha1
                ├── artifacta_2.10-1.0.0-SNAPSHOT.pom
                ├── artifacta_2.10-1.0.0-SNAPSHOT.pom.md5
                └── artifacta_2.10-1.0.0-SNAPSHOT.pom.sha1

4 directories, 15 files
```

### ivy cache

```
$ tree ivy/cache/cache/org.example 
ivy/cache/cache/org.example
└── artifacta_2.10
    ├── ivy-1.0.0-SNAPSHOT.xml
    └── ivydata-1.0.0-SNAPSHOT.properties

1 directory, 2 files

$ cat ivy/cache/cache/org.example/artifacta_2.10/ivydata-1.0.0-SNAPSHOT.properties 
#ivy cached data file for org.example#artifacta_2.10;1.0.0-SNAPSHOT
#Sat Apr 05 01:08:20 PDT 2014
artifact\:artifacta_2.10\#src\#jar\#11539150.exists=true
artifact\:artifacta_2.10\#src\#jar\#11539150.location=/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT-sources.jar
artifact\:artifacta_2.10\#jar\#jar\#1628349114.is-local=true
artifact\:artifacta_2.10\#pom.original\#pom\#-182976915.is-local=true
artifact\:artifacta_2.10\#jar\#jar\#1628349114.location=/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT-tests.jar
artifact\:ivy\#ivy\#xml\#684068265.exists=true
artifact\:artifacta_2.10\#jar\#jar\#-1691211787.location=/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.jar
artifact\:artifacta_2.10\#jar\#jar\#-1835539577.exists=true
resolver=sbt-chain
artifact\:artifacta_2.10\#jar\#jar\#1628349114.exists=true
artifact\:artifacta_2.10\#jar\#jar\#-1691211787.exists=true
artifact\:artifacta_2.10\#src\#jar\#11539150.is-local=true
artifact\:ivy\#ivy\#xml\#684068265.is-local=true
artifact\:artifacta_2.10\#jar\#jar\#-1835539577.is-local=true
artifact\:ivy\#ivy\#xml\#684068265.location=/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.pom
artifact\:artifacta_2.10\#jar\#jar\#-1835539577.location=/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.jar
artifact\:artifacta_2.10\#jar\#jar\#-1691211787.is-local=true
artifact\:artifacta_2.10\#pom.original\#pom\#-182976915.location=/Users/eugene/work/quick-test/sbt-641/demo-repo/org/example/artifacta_2.10/1.0.0-SNAPSHOT/artifacta_2.10-1.0.0-SNAPSHOT.pom
artifact\:artifacta_2.10\#pom.original\#pom\#-182976915.exists=true
```
