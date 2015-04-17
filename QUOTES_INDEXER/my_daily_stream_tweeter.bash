echo 'Killing previous processes'
sudo kill $(ps aux | grep 'my_daily_stream_listening_and_trends_retweeting.jar' | awk '{print $2}');
echo 'Removing the previous log file'
sudo rm -r /tmp/my_daily_stream_listening_and_trends_retweeting.log;
echo 'Launching the new job'
sudo java -jar /home/sduprey/My_Executable/my_daily_stream_listening_and_trends_retweeting.jar;