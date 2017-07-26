#
# Regular cron jobs for the simdoc-server package
#
0 4	* * *	root	[ -x /usr/bin/simdoc-server_maintenance ] && /usr/bin/simdoc-server_maintenance
