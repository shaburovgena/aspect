//Утилита для работы с переменными окружения разных ОС
const path = require('path');
//Плагин для разделения модулей
const VueLoaderPlugin = require('vue-loader/lib/plugin');

module.exports = {
    mode: 'development',
    devtool: 'source-map',
    //Сборка проекта будет начинаться с указанных файлов
    entry: path.join(__dirname, 'src', 'main', 'resources', 'static', 'js', 'main.js'),
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 8001,
        allowedHosts: [
            'localhost:9090'
        ],
        //
        stats: 'errors-only',
        clientLogLevel: 'error',
    },
    module: {
        rules: [
            {//Пропускаем через babel все *.js файлы
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {//Пропускаем через vue-loader все *.vue файлы
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {//Все *.css файалы будут скомпанованы loaderom в один файл
                test: /\.css$/,
                use: [
                    'vue-style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.styl$/,
                loader: ['style-loader', 'css-loader', 'stylus-loader', {
                    loader: 'vuetify-loader',
                    options: {
                        theme: path.resolve('./node_modules/vuetify/src/stylus/theme.styl')
                    }
                }]
            },
            {
                test: /\.s(c|a)ss$/,
                use: [
                    'vue-style-loader',
                    'css-loader',
                    {
                        loader: 'sass-loader',
                        options: {
                            implementation: require('sass'),
                            fiber: require('fibers'),
                            indentedSyntax: true
                        }
                    }
                ]
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2)(\?\S*)?$/,
                loader: 'file-loader'
            }
        ]
    },
    plugins: [
        new VueLoaderPlugin()
    ],
    resolve: {
        modules: [
            path.join(__dirname, 'src', 'main', 'resources', 'static', 'js'),
            path.join(__dirname, 'node_modules'),
        ],
    }
}