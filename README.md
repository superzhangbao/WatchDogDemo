# WatchDogDemo
关闭Android系统看门狗的demo

1.关闭内核自动翻转看门狗电平
 	/sys/devices/platform/watchdog/enable
		写入：0（关闭）
		
2.翻转看门狗电平 
	/sys/devices/platform/watchdog/wacthdog	
		写入： 1 （高电平）
		写入： 0 （低电平）
		
操作：
	1.先将内核自动翻转看门狗关闭
	2.然后app中翻转看门狗电平，app中需循环翻转看门狗电平,翻转间隔时间小于30s。
