function build {
  if [ "$1" == "jphonelite-android" ]; then
    return
  fi
  if [ "$1" == "jfrdp" ]; then
    return
  fi
  cd $1
  ant jar
  sudo ant install -Dbits=32
  ant deb32
  cd ..
}

for i in *; do
  if [ -d $i ]; then
    build $i
  fi
done
