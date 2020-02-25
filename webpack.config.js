const webpack = require('webpack');
const path = require('path');

module.exports = {
  entry: './src/main/resources/static/js/app/index.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    publicPath: '/dist',
    filename: 'bundle.js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        include: path.join(__dirname),
        exclude: /(node_module)|(dist)/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['env']
          }
        }
      },
      {
        test: /\.mustache$/,
        use: {
          loader: 'mustache-loader'
        }
      }
    ]
  }
};
