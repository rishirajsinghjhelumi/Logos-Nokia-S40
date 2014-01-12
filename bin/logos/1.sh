for i in {1..12}
do
	mkdir level_$i
	cd level_$i
	wget http://www.logosquizwalkthrough.com/level-$i/
	grep "_tmb" index.html  | cut -d" " -f5 | cut -d"=" -f2 | cut -d"\"" -f2 > $i.out
	rm index.html
	files=`cat $i.out`
	for j in $files
	do
		echo $j
		wget $j
	done
	cat $i.out | cut -d"/" -f5  > level_$i.txt
	cd ..
done
