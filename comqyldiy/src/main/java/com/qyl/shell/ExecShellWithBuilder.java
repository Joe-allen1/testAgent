package com.qyl.shell;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ExecShellWithBuilder {
//    public static void main(String[] args) {
//        try {
//            // 构建命令列表
//            ProcessBuilder builder = new ProcessBuilder(Arrays.asList("sh", "/home/user/test.sh", "arg1", "arg2"));
//            builder.redirectErrorStream(true); // 合并错误流到输出流
//
//            Process process = builder.start();
//
//            // 读取输出，指定 UTF-8 编码
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//            String line;
//            StringBuilder output = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("输出：" + output.toString());
//            System.out.println("退出码：" + exitCode);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        String host = "10.0.10.115";  // 替换为内网服务器 IP
        String user = "root";  // 远程服务器用户名
        String password = "Admin@9000";  // 远程服务器密码（生产环境避免明文）
        String scriptPath = "/usr/local/Tianxun/model_bash/stop_model_qwen2.5-14b.sh";  // 脚本路径
        String command = "sh " + scriptPath  ;  // 执行命令，可加参数

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);  // 默认 SSH 端口 22
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");  // 忽略主机密钥检查（测试用，生产中添加 known_hosts）
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            // 处理输出流，指定 UTF-8 编码避免乱码
            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 处理错误流
            InputStream err = channel.getErrStream();
            BufferedReader errReader = new BufferedReader(new InputStreamReader(err, StandardCharsets.UTF_8));
            StringBuilder error = new StringBuilder();
            while ((line = errReader.readLine()) != null) {
                error.append(line).append("\n");
            }

            channel.disconnect();
            session.disconnect();

            System.out.println("脚本输出：" + output.toString());
            if (error.length() > 0) {
                System.out.println("错误输出：" + error.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
