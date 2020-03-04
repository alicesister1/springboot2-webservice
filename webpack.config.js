const webpack = require('webpack');
const path = require('path');

module.exports = (env) => {

  /**
  * 스프링부트 View 파일이 최종적으로 정적 리소스 경로에(src/main/resources/static)
  * 있어야 하지만 클라이언트 코드를 수정할 때마다 static 디렉토리에 아웃풋 파일이 반영되면
  * 핫스왑 기능이 활성화 되어 불필요한 컨텍스트 리로드가 발생할 수 있기 때문에
  * client 경로를 별도로 지정함
  */

  // symbolic link 연결 후 정적 파일 라이브로 반영됨
  let clientPath = path.resolve(__dirname, './src/main/client/js/app');
  let outputPath = path.resolve(__dirname, 'dist');

  return {
    mode: !env ? 'development' : env,
    entry: {
      index: clientPath + '/index.js'
    },
    output: {
      filename: '[name].js',
      path: outputPath,
    },
    devServer: {
      contentBase: outputPath,
      publicPath: '/',
//      host: '0.0.0.0',
      host: 'localhost',
      port: 8081,
      proxy: {
        '**': 'http://[::1]:8080',
      },
      inline: true,
      hot: false
    }
  }
};
