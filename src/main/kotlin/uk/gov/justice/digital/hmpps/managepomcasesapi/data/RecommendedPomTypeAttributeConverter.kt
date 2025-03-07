package uk.gov.justice.digital.hmpps.managepomcasesapi.data

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.RecommendedPomType
import java.util.Locale

@Converter(autoApply = true)
class RecommendedPomTypeAttributeConverter : AttributeConverter<RecommendedPomType, String> {
  override fun convertToDatabaseColumn(pomType: RecommendedPomType): String = pomType.name.lowercase(Locale.getDefault())
  override fun convertToEntityAttribute(dbData: String): RecommendedPomType = RecommendedPomType.valueOf(dbData.uppercase(Locale.getDefault()))
}
