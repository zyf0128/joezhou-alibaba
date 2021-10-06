# 开启IntelVt-x

**武技：** 安装 `vmware` 之前，先让主机的主板支持 `Virtualization Technology` 虚拟化技术。
- 技嘉主板：
	- 启动时按Del键进入BIOS，在 `BIOS Features` 下，键盘选择 `Intel Virtualization Technology`。
	- 按回车键，选择 `Enabled`，回车。 
	- 然后按F10，键盘选择 `Yes` 回车保存重启。
- 惠普笔记本：
	- 启动时按F10进入BIOS，选择 `system configuration`，点击 `Device Configurations`。
	- 将 `Virtualization technology` 的选项前打勾，点击 `Save`。
	- 点击 `File`，选择 `Save Changes And Exit`，保存退出。
- 华硕 `UEFI BIOS`：
	- 启动时按F2/F8/Del进BIOS，在 `Advanced` 下，选择 `CPU Configuration` 回车。
	- 找到 `Intel Virtualization Technology` 回车改成 `Enabled`，最后按F10保存重启。
- ThinkPad笔记本：
	- 启动时按F1/Fn+F1进入BIOS，切换到 `Security`，选择 `Virtualization`，回车。
	- 选中 `Intel(R) Virtualization Technology` 回车，改成 `Enabled`，最后按F10保存重启。
- Phoenix或InsydeH20主板：
	- 启动时按Del/F2/F1等按键进入BIOS。
	- Phoenix主板在 `Configuration`下，选择 `Intel Virtualization Technology` 回车改成 `Enabled`，按F10保存重启。
	- InsydeH20主板在 `Configuration` 下将 `Intel Virtualization Technology` 改成 `Enabled`，按F10保存重启。

# 安装vmware

**武技：** 一直下一步即可，产品秘钥任选其一：
- FF31K-AHZD1-H8ETZ-8WWEZ-WUUVA
- CV7T2-6WY5Q-48EWP-ZXY7X-QGUWD
- CG54H-D8D0H-H8DHY-C6X7X-N2KG6
- ZC3WK-AFXEK-488JP-A7MQX-XL8YF
- AC5XK-0ZD4H-088HP-9NQZV-ZG2R4
- ZC5XK-A6E0M-080XQ-04ZZG-YF08D
- ZY5H0-D3Y8K-M89EZ-AYPEG-MYUA8
- FF590-2DX83-M81LZ-XDM7E-MKUT4
- FF31K-AHZD1-H8ETZ-8WWEZ-WUUVA
- CV7T2-6WY5Q-48EWP-ZXY7X-QGUWD
- AALYG-20HVE-WHQ13-67MUP-XVMF3

# 安装centos

**流程：** 
- 打开vmware，点击 `创建新的虚拟机`。
- 选择 `自定义（高级）`，下一步。
- 硬件兼容选择 `Workstation 14.x`，下一步。
- 选择 `稍后安装操作系统`，下一步。
- 客户机操作系统选择 `Linux`，版本选择 `CentOS 7 64位`，下一步。
- 编辑虚拟机名称和位置（必须是空文件夹），下一步。
- 处理器数量选择1，内核数量选择2，下一步。
- 虚拟机内存分配1024MB，下一步。
- 配置网络类型，学习建议使用NAT网络：
	- 桥接网络，和主机在同一网段，本机联网时虚拟机就可以联网，虚拟机可与本机互通，也可与同网段内其他主机互通。
	- NAT网络：本机联网时虚拟机就可以联网，与本机互通，但与本机网段其他主机不互通。
	- 仅主机网络：虚拟机不能联网，与本机互通，与本机网段其他主机不互通。
- SCSI控制器选择 `LSI Logic`，下一步。
- 虚拟磁盘选择 `SCSI`，下一步。
- 选择 `创建虚拟磁盘`，下一步。
- 分配磁盘大小为20G，选择 `拆分磁盘`，下一步。
- 磁盘文件默认即可，下一步。
- 点击完成。

# 配置centos

**流程：** 
- 点击 `编辑虚拟机设置`，移除 `USB控制器`，`声卡` 和 `打印机`。
- 点击 `CD/DVD`，右侧选择 `使用ISO映像文件`，浏览到我们的ISO镜像，点击确定。
- 点击 `开启虚拟机`。
- 点击 `install centos7`，回车开始安装，根据提示需要按一下回车。
- 语言选择 `中文` 或 `English`，点击继续。
- 点击安装位置，点进来后选择 `我要配置分区`，点击左上角 `完成`。
- 点击 `+` 添加挂载点：
	- 为 `/boot` 分配1024MB。
	- 为 `swap` 分配4096MB。
	- 为 `/` 分配剩余空间，不填即可自动填写剩余空间。
	- 点击左上角 `完成`，弹框选择 `接受更改`。
- 点击网络和主机名，更改主机名，点击左上角 `完成`。
- 点击右下角 `开始安装`，安装时设置ROOT密码，建议ROOT，点击完成，等待安装。
- 重启后 输入账号密码登录。
- 修改网络配置：
	- `vi /etc/sysconfig/network-scripts/ifcfg-ens32`
	- 按 `i` 进入编辑模式，底部会有 `--INSERT--` 提示。
	- 修改 `onboot=yes`，表示网络服务启动的时候，自动分配ip，因为Linux默认采用动态ip的方式。
	- 如果桥接模式：除了修改 `onboot=yes` 外：
		- `bootproto=static`
		- `ipaddr=虚拟机IP网段`，和主机处于同一网段
		- `netmask=子网掩码`
		- `gateway=网关`
		- `dns1=首选DNS`
	- `service network restart`：刷新网络配置
	- `ping www.baidu.com` 测试
	- `id addr`：查看ens32中的inet网络信息
- 添加 `ifconfig` 源：该命令用于查看当前虚拟机的网络配置信息：
	- `yum search ifconfig`：查找 `ifconfig` 命令的源包。
	- `yum -y net-tools`：下载 `ifconfig` 命令的源包。
	- `yum search ifconfig`：查看到网络配置信息中的虚拟机IP
	- 测试主机DOS可ping通虚拟机IP，虚拟机可ping通主机IP。

# 安装VMware Tools

**流程：** 以root身份登录虚拟机：
- 点击VMware菜单栏 `虚拟机`，选择 `安装VMware Tools`。
- `ls /dev`：查看 `dev` 中是否含有 `cdrom` 目录。
- `ls /mnt`：查看 `mnt` 中是否含有 `cdrom` 目录。
- `mkdir /mnt/cdrom`：在 `mnt` 中创建 `cdrom` 目录。
- `ls /mnt`：再次查看 `mnt` 中是否含有 `cdrom` 目录。
- `mount -t iso9660 /dev/cdrom /mnt/cdrom`：将光盘挂载到 `/mnt/cdrom` 目录。
- `ls /mnt/cdrom`：查看是否挂载成功，即存在VMwareTools-9.9.3-2759765.tar.gz文件。
- `cp /mnt/cdrom/VMwareTools-9.9.3-2759765.tar.gz ~`：将后缀名为tar.gz的文件拷贝到根目录。
- `ls ~`：查看根目录中是否复制成功，即存在VMwareTools-9.9.3-2759765.tar.gz文件。
- `umount /dev/cdrom`：解除挂载。
- `tar -zxvf VMwareTools-9.9.3-2759765.tar.gz`：解压安装包。
- `ls ~`：查看是否解压成功，`vmware-tools-distrib` 是源码文件目录。
- `cd vmware-tools-distrib`：进入到解压后的源码文件目录。
- `yum -y install perl gcc gcc-c++ make cmake kernel kernel-headers kernel-devel net-tools` 安装编译环境：
    - 若出现 `Couldn’t resolve host 'mirrorlist.centos.org` 表示没有正确配置网络，需要先配置好网络。
- `./vmware-install.pl`：在 `vmware-tools-distrib` 目录运行安装文件，进入安装流程：
    - `Do you still want to proceed with this installation?`：输入yes继续。
    - `Do you wish to continue?`：输入yes继续。
    - `In which dir do you want to install the binary files?`：直接回车，将二进制文件装在 `/usr/bin` 中。
    - `What is the dir that contains the init dirs (rc0.d/ to rc6.d/)?`：直接回车，将初始化目录存入 `/etc/rc.d` 中。
    - `What is the dir that contains the init scripts?`：直接回车，将初始化脚本存入 `/etc/rc.d/init.d` 中。
    - `In which dir do you want to install the daemon files?`：直接回车，将守护文件装在 `/usr/sbin`。
    - `In which dir do you want to install the library files?`：直接回车，将库文件装在 `/usr/lib/vmware-tools`。
    - `Is this what you want?`：输入yes继续。
    - `In which dir do you want to install the common agent library files`：直接回车，将通用库装在 `/usr/lib`。
    - `In which dir do you want to install the common agent transient files`：直接回车，将通用瞬态文件装在 `/var/lib`。
    - `In which dir do you want to install the documentation files`：直接回车，将文档装在 `/usr/share/doc/vmware-tools`。
    - `Is this what you want?`：输入yes继续。
    - `Do you want this program to invoke the command for you now?`：输入yes，让程序调用 `vmware-config-tools.pl` 命令。
    - `Do you wish to enable this feature?`：输入yes，开启主机和虚拟机文件共享功能。
    - `Would you like to change it?`：输入no，使用 `/bin/gcc` 目录作为gcc二进制目录。
    - `Would you like to change it?`：输入no，使用 `""` 作为内核头目录，一直回车进行确认。
    - `Do you wish to enable this feature?`：输入yes，开启主机和虚拟机文件拖拽和复制功能。
    - `Would you like to enable vmware automatic kernel modules`：输入yes，开启自动内核构建功能。
    - `Do you want to enable Guest Authentication?`：输入yes，开启虚拟机验证，关闭后无法使用通用。
    - `Do you want to enable Commen Agent?`：输入yes，开启通用代理。
    - `Enjoy`：出现成功提示。




- 若出现 `Enter the path to the kernel header files for the 3.10.0-327.el7.x86_64 kernel?`，因尚未创建相应的软链接导致的，故先用 `ctrl+c` 结束安装。
- `yum -y install perl gcc gcc-c++ make cmake kernel kernel-headers kernel-devel net-tools`
- `rpm -aq | grep kernel-headers` 查看kernel-headers是否安装成功
- `rpm -ql kernel-headers-3.10.0-957.21.2.el7.x86_64|less` 查看安装目录，`|less` 用于ctrl+pageup/pagedown翻页。
    - 可知当前的安装目录是 `/usr/include/linux/`，找到 `version.h` 文件，为其创建软链接
    - `cd /usr/include/linux` 进入安装目录
    - yum默认安装目录是 `/usr/src`，故现为 `/usr/src` 里面的 `version.h` 创建软链接
    - `ln -s /usr/src/kernels/3.10.0-514.21.2.el7.x86_64/include/generated/uapi/linux/version.h /usr/src/kernels/3.10.0-514.21.2.el7.x86_64/include/linux/version.h` 
    - `reboot` 重启系统。
- `vmware-tools-distrib` 源码包目录进行再次重新安装 VMware Tools，这次没有报错直接安装成功了
- `/usr/bin/vmware-config-tools.pl` 命令配置VMware Tools，按回车键直接运行
- `/usr/bin/vmware-user` 启动vmware用户进程
- `startx` 启动图形界面
- 完成了VMware tools的安装工作

# VMware共享文件夹

**概念：**
- 点击工具栏的 `虚拟机` - `设置` - `选项` - `共享文件夹`
- `cd /mnt/hgfs`
