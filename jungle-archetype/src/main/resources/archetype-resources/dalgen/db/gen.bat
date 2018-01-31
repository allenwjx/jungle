@echo off

rem 用于设置命令行窗口大小
IF DEFINED modeCmdExecuted GOTO START
mode con cols=120 lines=3000
set modeCmdExecuted="true"

:START
rem set variable for ORACLE "no ocijdbc10 in java.library.path" --error
rem set NLS_LANG="SIMPLIFIED CHINESE_CHINA.ZHS16GBK"
set PATH=%ORACLE_HOME%;%ORACLE_HOME%\lib\win32;%PATH%
set TNS_ADMIN=%ORACLE_HOME%\network\admin\tnsnames.ora

echo ----------------------------------------------------------------------------
echo 命令行使用: 
echo gen dal [table_sql_name] ,根据数据库表的配置文件生成代码,(需要有xml配置文件)
echo gen table [table_sql_name] 根据数据库表的生成代码,可以生成dalgen的配置文件(不需要xml配置文件)
echo gen seq : 生成oracle sequence SeqDAO生成代码

mvn groovy:execute -DgeneratorConfigFile=gen_config.xml -DexecuteTarget=%1 -DgenInputCmd=%2  --errors

