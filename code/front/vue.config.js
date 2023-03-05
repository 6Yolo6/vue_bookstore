const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  publicPath: './',
  lintOnSave: false,
  transpileDependencies: true,
  devServer: {
    // disableHostCheck: true,
    host: '0.0.0.0', // 将此处localhost 修改为  '0.0.0.0',0.0.0.0意义相当于*，表示所有
    port: 8080, 
    historyApiFallback: true,
    allowedHosts: "all"
  }
})
