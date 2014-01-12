for i in {1..12}
do
	cd level_$i
	cat level_$i.txt | cut -d"_" -f1 > levelAnswers_$i.txt
	cd ..
done
