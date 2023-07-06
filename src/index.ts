import * as express from 'express'

abstract class App {
    static get port(): number {
        return 8080
    }

    static run(): void {
        const server = express()
        server.get('/route', App.route)
        server.get('/', (req, res) => {
            res.send('call /route?from=...&to=... to find a route')
        })
        server.listen(App.port, () => {
            console.log('Listening on port ' + App.port)
        })
    }

    static route(request: express.Request, response: express.Response) {
        return response.send('not implemented yet')
    }
}

App.run()