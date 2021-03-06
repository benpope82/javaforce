function build {
  if [ "$1" == "jrepo" ]; then
    return
  fi
  if [ "$1" == "jphonelite-android" ]; then
    return
  fi
  if [ "$1" == "jfrdp" ]; then
    return
  fi
  cd $1
  ant jar
  sudo ant install -Dbits=32
  if [ "$1" == "jflogon" ]; then
    sudo ant rpm32
  else
    ant rpm32
  fi
  cd ..
}

for i in *; do
  if [ -d $i ]; then
    build $i
  fi
done
