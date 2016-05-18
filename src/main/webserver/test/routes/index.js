/* eslint-env mocha */

import supertest from "supertest";
import app from "../../app.js";

const request = supertest(app);

describe("Routes: Index", () => {
    describe("GET /", () => {
        it("returns the Home Page", done => {
            request.get("/")
                .expect(200)
                .end((err) => done(err));
        });
    });
});
