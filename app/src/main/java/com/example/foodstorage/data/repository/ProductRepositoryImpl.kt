package com.example.foodstorage.data.repository

import com.example.foodstorage.data.database.ProductDao
import com.example.foodstorage.data.mapper.ProductMapper
import com.example.foodstorage.domain.Product
import com.example.foodstorage.domain.RepositoryResult
import com.example.foodstorage.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val mapper: ProductMapper
) : ProductRepository {


    override suspend fun saveProduct(product: Product): RepositoryResult<Unit> {

        val entity = mapper.toEntity(product)
        dao.insert(entity)
        return RepositoryResult.Success(Unit)

    }

    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAll().map { entities ->
            entities.map { entity ->
                mapper.toDomain(entity)
            }
        }
    }

    override suspend fun getProductsOnce(): List<Product> {

        return dao.getProductsOnce().map {entity ->
            mapper.toDomain(entity)
        }
    }

    override suspend fun getProductById(id: Int): Product{
        return dao.getProductById(id).let { mapper.toDomain(it) }
    }

    override suspend fun deleteProduct(id: Int): RepositoryResult<Unit> {
        val deleteRows = dao.delete(id)
        return if (deleteRows == 0) {
            RepositoryResult.Error("Продукт не найден")

        } else {
            RepositoryResult.Success(Unit)
        }

    }

    override suspend fun updateProduct(product: Product): RepositoryResult<Unit> {
        dao.update(mapper.toEntity(product))
        return RepositoryResult.Success(Unit)
    }

}
