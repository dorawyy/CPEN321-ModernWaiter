const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)
const recommendation_logic = require('../recommendation_logic')

describe('Test getItemRecommendation() with mock recommendation logic', () => {
    it('Gets the recommended item with valid user and restaurant', async done => {
        // Arrange
        const userId = 1
        const restaurantId = 1
        const url = `/recommendation/${userId}/${restaurantId}`
        const expectedItemId = 5
        const mock = jest.fn().mockReturnValue(expectedItemId)
        recommendation_logic.getRecommendation = mock

        // Act
        const response = await request.get(url)

        // Assert
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual({itemId : expectedItemId})
        done()
      })

      it('Gets the recommended item with another valid user and restaurant', async done => {
        // Arrange
        const userId = 2
        const restaurantId = 1
        const url = `/recommendation/${userId}/${restaurantId}`
        const expectedItemId = 3
        const mock = jest.fn().mockReturnValue(expectedItemId)
        recommendation_logic.getRecommendation = mock

        // Act
        const response = await request.get(url)

        // Assert
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual({itemId : expectedItemId})
        done()
      })

      it('Gets the recommended item with valid user and invalid restaurant', async done => {
        // Arrange
        const userId = 1
        const restaurantId = 5
        const url = `/recommendation/${userId}/${restaurantId}`
        const mock = jest.fn().mockReturnValue()
        recommendation_logic.getRecommendation = mock

        // Act
        const response = await request.get(url)

        // Assert
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual({})
        done()
      })

      it('Gets the recommended item with invalid user and valid restaurant', async done => {
        // Arrange
        const userId = 10
        const restaurantId = 1
        const url = `/recommendation/${userId}/${restaurantId}`
        const mock = jest.fn().mockReturnValue()
        recommendation_logic.getRecommendation = mock

        // Act
        const response = await request.get(url)

        // Assert
        expect(response.status).toBe(400)
        expect(response.body).toStrictEqual({"message": "Failed to get user preference, check if userId is valid"})
	      done()
      })
  })

  
